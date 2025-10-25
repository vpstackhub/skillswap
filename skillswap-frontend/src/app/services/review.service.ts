import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ReviewService {
  private baseUrl = `${environment.apiUrl}/reviews`;

  constructor(private http: HttpClient) {}

  createReview(review: any, token: string): Observable<any> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.post(this.baseUrl, review, { headers });
  }

  getReviewsForTeacher(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/teacher/${id}`);
  }

  getAverageRatingForTeacher(teacherId: number, token: string) {
    return this.http.get<number>(`${this.baseUrl}/teacher/${teacherId}/average`, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }

  //  Added for Admin dashboard
  getAllReviews(): Observable<any[]> {
    const token = localStorage.getItem('token') || '';
    return this.http.get<any[]>(`${this.baseUrl}`, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }
}
