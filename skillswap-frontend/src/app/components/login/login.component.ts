import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router'; // ✅ Add RouterLink here
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink], 
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

    onSubmit(): void {
    if (this.loginForm.invalid) return;

    this.errorMessage = null;

    this.authService.login(this.loginForm.value).subscribe({
      next: (user) => {
        // ✅ user now contains role + email
        if (!user || !user.role) {
          this.router.navigate(['/classes']);
          return;
        }

        switch (user.role) {
          case 'TEACHER':
            this.router.navigate(['/dashboard']);
            break;
          case 'ADMIN':
            this.router.navigate(['/dashboard']);
            break;
          default:
            this.router.navigate(['/classes']);
        }
      },
      error: (err) => {
        this.errorMessage = err?.error?.error || 'Login failed';
      }
    });
  }
}
