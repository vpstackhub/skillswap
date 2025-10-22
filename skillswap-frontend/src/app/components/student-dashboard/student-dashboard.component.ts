import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookingService } from '../../services/booking.service';
import { AuthService, User } from '../../services/auth.service';
import { RouterLink } from '@angular/router';


@Component({
  selector: 'app-student-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './student-dashboard.component.html',
  styleUrls: ['./student-dashboard.component.css']
})
export class StudentDashboardComponent implements OnInit {
  user: User | null = null;
  sClasses: string[] = [];
  sSchedule: any[] = [];
  sMessages = [
    { from: 'Prof. Smith', content: 'Assignment due next week.' },
    { from: 'Admin', content: 'New event on campus.' }
  ];

  constructor(private bookingService: BookingService, private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.me().subscribe({
      next: (user) => {
        this.user = user;
        if (user?.role === 'STUDENT') {
          const token = localStorage.getItem('token') || '';
          this.bookingService.getBookingsByStudent(user.id!, token).subscribe({
            next: (bookings) => {
              this.sClasses = bookings.map((b: any) => b.topic);
              this.sSchedule = bookings.map((b: any) => ({
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
