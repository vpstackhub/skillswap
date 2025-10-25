import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ReviewService } from '../../services/review.service';
import { AuthService } from '../../services/auth.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { TeacherService, Teacher } from '../../services/teacher.service';


@Component({
  selector: 'app-review',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent implements OnInit, OnDestroy {
  rating = 0;
  comment = '';
  classId: number | null = null;
  className = '';
  teacherId: number | null = null;
  teacherName: string = '';
  studentId: number | null = null;
  showToast = false;
  averageRating: number | null = null;
  isSubmitting = false;

  private destroy$ = new Subject<void>();
  teachers: Teacher[] = [];

  constructor(
    private route: ActivatedRoute,
    private reviewService: ReviewService,
    private authService: AuthService,
    private teacherService: TeacherService
  ) {}

  ngOnInit(): void {
    // ✅ Read query params
    this.route.queryParams.pipe(takeUntil(this.destroy$)).subscribe(params => {
      if (params['classId']) this.classId = +params['classId'];
      if (params['className']) this.className = params['className'];
      if (params['teacherId']) this.teacherId = +params['teacherId'];
      if (params['teacherName']) this.teacherName = params['teacherName'];

      // ✅ Fetch current average rating once teacherId is known
      if (this.teacherId) {
        const token = localStorage.getItem('token') || '';
        this.reviewService
          .getAverageRatingForTeacher(this.teacherId, token)
          .subscribe(avg => (this.averageRating = avg));
      }
    });

    // ✅ Identify logged-in student
    this.authService.currentUser$
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => {
        if (user?.role === 'STUDENT') this.studentId = user.id!;
      });
      // ✅ Load all teachers
    this.teacherService.getAllTeachers().subscribe({
      next: (data) => (this.teachers = data),
      error: (err) => console.error('Error loading teachers', err)
    });     
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  submitReview(): void {
    if (!this.studentId || !this.teacherId || this.rating < 1) {
      alert('Please select a rating and ensure teacher ID is available.');
      return;
    }

    const review = {
      studentId: this.studentId,
      teacherId: this.teacherId,
      classId: this.classId,
      className: this.className,
      rating: this.rating,
      comment: this.comment,
      createdAt: new Date().toISOString()
    };

    const token = localStorage.getItem('token') || '';
    this.isSubmitting = true;

    this.reviewService.createReview(review, token).subscribe({
      next: () => {
        this.showToast = true;
        this.comment = '';
        this.rating = 0;
        this.isSubmitting = false;

        // ✅ Refresh average after submission
        if (this.teacherId) {
          this.reviewService
            .getAverageRatingForTeacher(this.teacherId, token)
            .subscribe(avg => (this.averageRating = avg));
        }

        setTimeout(() => (this.showToast = false), 3000);
      },
      error: () => (this.isSubmitting = false)
    });
  }
}
