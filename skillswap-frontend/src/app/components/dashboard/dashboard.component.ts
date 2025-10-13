import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService, User } from '../../services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  user: User | null = null;
  loading = true;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit(): void {
    //  get current user from backend
    this.authService.me().subscribe({
      next: (res) => {
        this.user = res;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.router.navigate(['/login']); // invalid or expired session
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  // for Student > Classes with dummy data

  sClasses = ['Math 101', 'English Literature', 'Physics', 'Computer Science'];
  
  sSchedule = [
    { day: 'Monday', subject: 'Math 101', time: '10:00 AM' },
    { day: 'Tuesday', subject: 'Physics', time: '12:00 PM' },
    { day: 'Wednesday', subject: 'English Literature', time: '9:00 AM' },
    { day: 'Friday', subject: 'Computer Science', time: '2:00 PM' }
  ];

  // for Student > Messages with dummy data

  sMessages = [
    { from: 'Prof. Smith', content: 'Assignment due next week.' },
    { from: 'Admin', content: 'New event on campus.' }
  ];


  // for Teachers > My Courses with dummy data

  tCourses = ['Math 101', 'English Literature', 'Physics', 'Computer Science'];
  
  tSchedule = [
    { day: 'Monday', subject: 'Math 101', time: '10:00 AM' },
    { day: 'Tuesday', subject: 'Physics', time: '12:00 PM' },
    { day: 'Wednesday', subject: 'English Literature', time: '9:00 AM' },
    { day: 'Friday', subject: 'Computer Science', time: '2:00 PM' }
  ];

  // for Student > Messages with dummy data

  tMessages = [
    { from: 'Prof. Smith', content: 'Assignment due next week.' },
    { from: 'Admin', content: 'New event on campus.' }
  ];
}
