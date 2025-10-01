import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export type UserRole = 'STUDENT' | 'TEACHER' | 'ADMIN';

interface RegisterRequest {
  email: string;
  password: string;
  confirmPassword?: string;
  role: string;
}

interface LoginResponse {
  token: string;
  email: string;
  role: UserRole;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/users`; // e.g. http://localhost:8081/api/users

  register(userData: RegisterRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData);
  }

  /** Calls backend /api/users/login and stores the session */
  login(credentials: { email: string; password: string }): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(res => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('role',  res.role);
        localStorage.setItem('email', res.email); // <-- use 'email' key (not 'user')
      })
    );
  }

  /** Rehydrate session on refresh */
  me() {
    return this.http.get<{ id:number; name:string; email:string; role:UserRole }>(`${this.apiUrl}/me`);
  }

  /** Auth helpers */
  isLoggedIn(): boolean { return !!this.token; }
  get token(): string | null { return localStorage.getItem('token'); }
  get role(): UserRole | null { return localStorage.getItem('role') as UserRole | null; }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('email');
  }
}