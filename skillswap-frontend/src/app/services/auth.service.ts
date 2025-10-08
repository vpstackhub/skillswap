import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export type UserRole = 'STUDENT' | 'TEACHER' | 'ADMIN';

interface RegisterRequest {
  name: string;
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

export interface User {
  id?: number;
  name?: string;
  email: string;
  role: UserRole;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/users`;

  // 👇 new: central user subject
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor() {
    this.rehydrateSession(); // run on startup
  }

  register(userData: RegisterRequest): Observable<any> {
  return this.http.post(`${this.apiUrl}/register`, userData, { responseType: 'text' });
}

  login(credentials: { email: string; password: string }): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(res => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('role', res.role);
        localStorage.setItem('email', res.email);

        // update BehaviorSubject
        this.currentUserSubject.next({ email: res.email, role: res.role });
      })
    );
  }

  /** Hit backend /me to confirm user from token */
  me(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/me`).pipe(
      tap(user => this.currentUserSubject.next(user))
    );
  }

  /** Helpers */
  isLoggedIn(): boolean {
    return !!this.token;
  }
  get token(): string | null {
    return localStorage.getItem('token');
  }
  get role(): UserRole | null {
    return localStorage.getItem('role') as UserRole | null;
  }

  /** Quick getters for session data */
  getCurrentUser(): { email: string | null; role: string | null; token: string | null } {
    return {
      email: localStorage.getItem('email'),
      role: localStorage.getItem('role'),
      token: localStorage.getItem('token')
    };
  }

  get userEmail(): string | null {
    return localStorage.getItem('email');
  }

  get userRole(): string | null {
    return localStorage.getItem('role');
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('email');
    this.currentUserSubject.next(null);
  }

  /** Rehydrate session from stored token */
  private rehydrateSession() {
    if (this.token) {
      this.me().subscribe({
        next: user => this.currentUserSubject.next(user),
        error: () => this.logout() // invalid token → logout
      });
    }
  }
}
