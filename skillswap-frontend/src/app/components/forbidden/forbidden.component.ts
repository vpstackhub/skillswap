import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-forbidden',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="container text-center mt-5">
      <h1 class="display-4 text-danger">403</h1>
      <h2>Access Forbidden</h2>
      <p class="lead">You don’t have permission to view this page.</p>
      <a routerLink="/login" class="btn btn-primary mt-3">Go to Login</a>
    </div>
  `
})
export class ForbiddenComponent {}
