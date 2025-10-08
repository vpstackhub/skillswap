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
}
