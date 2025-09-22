import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.registerForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      role: ['STUDENT', Validators.required]
    });
  }

  onSubmit() {
    if (this.registerForm.invalid) {
      console.warn('Form is invalid');
      return;
    }

    const { name, email, password, confirmPassword } = this.registerForm.value;

    if (password !== confirmPassword) {
      alert('Passwords do not match');
      return;
    }

    const payload = { name, email, password }; // backend expects this parameters
    console.log('📡 Sending payload:', payload);

    this.authService.register(payload as any).subscribe({
      next: (res) => {
        console.log('✅ Registered:', res);
        alert('Registration successful!');
      },
      error: (err) => {
        console.error('❌ Register failed:', err);
        alert('Registration failed');
      }
    });
  }
}

  

