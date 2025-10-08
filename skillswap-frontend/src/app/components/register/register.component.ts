import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';


@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,RouterLink, RouterLinkActive],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.registerForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      role: ['STUDENT', Validators.required]
    });
  }

  errorMessage: string | null = null;

  onSubmit() {
    if (this.registerForm.invalid) {
      console.warn('Form is invalid');
      return;
    }

    const { name, email, password, confirmPassword, role } = this.registerForm.value;

    if (password !== confirmPassword) {
      alert('Passwords do not match');
      return;
    }

    this.authService.register({ name, email, password, confirmPassword, role } as any).subscribe({
      next: () => {
        alert('Registration successful!');
        this.router.navigate(['/login']); // or window.location.href = '/login'
      },
      error: (err) => {
        console.error('❌ Register failed:', err);

        // Reset any previous message
        this.errorMessage = null;

        if (err.error instanceof Blob) {
          err.error.text().then((text: string) => {
            const msg = text || 'Registration failed.';
            if (msg.includes('Only ADMIN')) {
              this.errorMessage = '🚫 Teacher registration is restricted. Only an admin can create teacher accounts.';
            } else {
              this.errorMessage = msg;
            }
          });
        } else if (typeof err.error === 'string') {
          this.errorMessage = err.error;
        } else if (err.error?.message) {
          this.errorMessage = err.error.message;
        } else {
          this.errorMessage = 'Registration failed. Please try again.';
        }
      }
    });
  }
}
