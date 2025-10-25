import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ReviewService } from '../../services/review.service';

@Component({
  selector: 'app-review-admin',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './review-admin.component.html',
  styleUrls: ['./review-admin.component.css']
})
export class ReviewAdminComponent implements OnInit {
  reviews: any[] = [];

  constructor(private reviewService: ReviewService) {}

  ngOnInit(): void {
    this.reviewService.getAllReviews().subscribe({
      next: (data: any[]) => (this.reviews = data),
      error: (err: any) => console.error('Error loading reviews', err)
    });
  }
}
