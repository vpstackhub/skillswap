import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ClassService } from '../../services/class.service';
import { Class } from '../../models/class.model';

@Component({
  selector: 'app-class-detail',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container mt-4" *ngIf="classData">
      <h2>{{ classData.title }}</h2>
      <p>{{ classData.description }}</p>
      <p><strong>Category:</strong> {{ classData.category }}</p>
      <p><strong>Teacher:</strong> {{ classData.teacher }}</p>
      <p><strong>Schedule:</strong> {{ classData.schedule }}</p>
    </div>
  `
})
export class ClassDetailComponent implements OnInit {
  classData?: Class;

  constructor(private route: ActivatedRoute, private classService: ClassService) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.classService.getClassById(id).subscribe(data => {
      this.classData = data;
    });
  }
}
