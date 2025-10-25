import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TeacherService } from '../../services/teacher.service';

@Component({
  selector: 'app-teacher-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './teacher-admin.component.html',
  styleUrls: ['./teacher-admin.component.css']
})
export class TeacherAdminComponent implements OnInit {
  teachers: any[] = [];
  newTeacher = { name: '', email: '', password: '' };

  constructor(private teacherService: TeacherService) {}

  ngOnInit(): void {
    this.loadTeachers();
  }

  loadTeachers(): void {
    this.teacherService.getAllTeachers().subscribe({
      next: data => (this.teachers = data),
      error: err => console.error('Error loading teachers', err)
    });
  }

  createTeacher(): void {
    if (!this.newTeacher.name || !this.newTeacher.email || !this.newTeacher.password) {
      alert('All fields are required.');
      return;
    }

    this.teacherService.createTeacher(this.newTeacher).subscribe({
      next: () => {
        this.loadTeachers();
        this.newTeacher = { name: '', email: '', password: '' };
      },
      error: err => console.error('Error creating teacher', err)
    });
  }

  deleteTeacher(id: number): void {
    if (!confirm('Are you sure you want to delete this teacher?')) return;

    this.teacherService.deleteTeacher(id).subscribe({
      next: () => (this.teachers = this.teachers.filter(t => t.id !== id)),
      error: err => console.error('Error deleting teacher', err)
    });
  }
}
