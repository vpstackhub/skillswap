import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookingService } from '../../services/booking.service';
import { AuthService, User } from '../../services/auth.service';

@Component({
  selector: 'app-teacher-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './teacher-dashboard.component.html',
  styleUrls: ['./teacher-dashboard.component.css']
})
export class TeacherDashboardComponent implements OnInit {
  user: User | null = null;
  tCourses: string[] = [];
  tSchedule: any[] = [];
  tMessages = [
    { from: 'Admin', content: 'You have a new booking request.' },
    { from: 'System', content: 'Your profile was viewed by 3 students today.' }
  ];

  constructor(private bookingService: BookingService, private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.me().subscribe({
      next: (user) => {
        this.user = user;
        if (user?.role === 'TEACHER') {
          const token = localStorage.getItem('token') || '';
          this.bookingService.getBookingsByTeacher(user.id!, token).subscribe({
            next: (bookings) => {
              this.tCourses = bookings.map((b: any) => b.topic);
              this.tSchedule = bookings.map((b: any) => ({
                day: new Date(b.dateTime).toLocaleDateString('en-US', { weekday: 'long' }),
                subject: b.topic,
                time: new Date(b.dateTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
              }));
            }
          });
        }
      }
    });
  }
}
