import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router, UrlTree } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean | UrlTree {
    const token = localStorage.getItem('token');
    const role  = (localStorage.getItem('role') || '').toUpperCase();

    // Not logged in → go to login
    if (!token || !role) return this.router.parseUrl('/login');

    // If the route doesn't declare roles, allow
    const allowed: string[] = route.data?.['roles'] ?? [];
    if (allowed.length === 0) return true;

    // If user has an allowed role, allow
    if (allowed.includes(role)) return true;

    // Single-dashboard setup: disallowed roles → login
    return this.router.parseUrl('/login');
  }
}