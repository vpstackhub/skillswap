import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { ClassService } from '../../services/class.service';
import { Class } from '../../models/class.model';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-class-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './class-list.component.html',   // <-- external file instead of inline
  styleUrls: ['./class-list.component.css']
})

export class ClassListComponent implements OnInit {
  classes: Class[] = [];
  userEmail = localStorage.getItem('email');
  userRole = localStorage.getItem('role');

constructor(
  private classService: ClassService,
  private router: Router,
  private authService: AuthService
) {}

  ngOnInit() {
    this.classService.getClasses().subscribe(data => {
      this.classes = data;
    });
  }

  logout(): void {
  this.authService.logout();
  this.router.navigate(['/login']);
}

}
