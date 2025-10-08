import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AuthGuard } from './guards/auth.guard';
import { RoleGuard } from './guards/role.guard';
import { ClassListComponent } from './components/class-list/class-list.component';
import { ClassDetailComponent } from './components/class-detail/class-detail.component';
import { ForbiddenComponent } from './components/forbidden/forbidden.component';


export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  {
    path: 'classes',
    component: ClassListComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['STUDENT'] }
  },
  {
    path: 'classes/:id',
    component: ClassDetailComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['STUDENT'] }
  },

  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['STUDENT', 'TEACHER'] }
  },

  { path: 'forbidden', component: ForbiddenComponent },

  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
