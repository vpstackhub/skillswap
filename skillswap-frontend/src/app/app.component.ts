import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet, RouterLink } from '@angular/router';
import { AuthService, User } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  user: User | null = null;

  constructor(private authService: AuthService, public router: Router) {
    this.authService.currentUser$.subscribe(u => this.user = u);
  }

  logout() {
    this.authService.logout();
    this.user = null;
    this.router.navigate(['/login']);
  }
}
