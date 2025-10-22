import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BookingService } from '../../services/booking.service';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-booking',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.css']
})
export class BookingComponent implements OnInit {
  showToast = false;
  lastBookingSummary: any = null;
  submitting = false;

  bookingData = {
    student: { id: null as number | null },
    teacher: { id: null as number | null },
    dateTime: '',
    topic: '',
    status: 'PENDING'
  };

  constructor(
    private bookingService: BookingService,
    private authService: AuthService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    //  Automatically fill student ID from logged-in user
    this.authService.currentUser$.subscribe((user) => {
      if (user?.role === 'STUDENT') {
        this.bookingData.student.id = user.id!;
        console.log('✅ Logged-in student ID:', user.id);
      }
    });
  }

  submit() {
    console.log('DEBUG bookingData:', this.bookingData);

    //  Guard for missing fields
    if (
      !this.bookingData.student.id ||
      !this.bookingData.teacher.id ||
      !this.bookingData.dateTime ||
      !this.bookingData.topic
    ) {
      console.warn('⚠️ Missing required fields');
      return;
    }

    this.submitting = true;
    const token = localStorage.getItem('token') || '';

    this.bookingService.createBooking(this.bookingData, token).subscribe({
      next: () => {
        this.submitting = false;
        this.showToast = true;

        //  Fetch teacher name from backend
        const teacherId = this.bookingData.teacher.id;
        this.http
          .get<any>(`${environment.apiUrl}/teacher/${teacherId}`)
          .subscribe({
            next: (teacher) => {
              this.lastBookingSummary = {
                topic: this.bookingData.topic,
                dateTime: this.bookingData.dateTime,
                teacherName: teacher.name
              };
            },
            error: () => {
              this.lastBookingSummary = {
                topic: this.bookingData.topic,
                dateTime: this.bookingData.dateTime,
                teacherName: `ID ${teacherId}`
              };
            }
          });

        // ✅ Auto-hide toast after 3.5 seconds
        setTimeout(() => (this.showToast = false), 3500);
      },
      error: (err) => {
        this.submitting = false;
        console.error('Booking failed', err);
      }
    });
  }
}
