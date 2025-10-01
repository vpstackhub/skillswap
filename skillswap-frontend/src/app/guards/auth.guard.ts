import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root' // globally available
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role'); // ✅ read role from storage

    if (token && role) {
      const allowedRoles = route.data['roles'] as Array<string>; // ✅ roles from route config
      if (!allowedRoles || allowedRoles.includes(role)) {
        return true; // ✅ user has permission
      }
    }

    // ❌ Unauthorized → back to login
    this.router.navigate(['/login']);
    return false;
  }
}