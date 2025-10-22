import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class BookingService {
  private apiUrl = `${environment.apiUrl}/bookings`;

  constructor(private http: HttpClient) {}

  getBookingsByTeacher(teacherId: number, token: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/teacher/${teacherId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
  }

  getBookingsByStudent(studentId: number, token: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/student/${studentId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
  }

  createBooking(payload: any, token: string) {
    return this.http.post(`${this.apiUrl}`, payload, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }
}