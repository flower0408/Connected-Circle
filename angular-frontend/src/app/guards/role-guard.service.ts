import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot } from '@angular/router';
import { AuthenticationService } from 'src/app/user/services/authentication.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RoleGuardService implements CanActivate{

  constructor(
		public auth: AuthenticationService,
		public router: Router
	) { }

	canActivate(
		route: ActivatedRouteSnapshot,
		state: RouterStateSnapshot
	  ): boolean | Observable<boolean> | Promise<boolean> {
		const expectedRoles: string = route.data['expectedRoles'];
		const token = localStorage.getItem('user');
		const jwt: JwtHelperService = new JwtHelperService();

		console.log(expectedRoles);

		if (!token) {
		  this.router.navigate(['']);
		  return false;
		}

		const info = jwt.decodeToken(token);


		// Check if info.role is defined and contains at least one element
		if (info && info.role) {
		  const roles: string[] = expectedRoles.split('|', 3);

		  if (roles.indexOf(info.role.authority.replace('ROLE_', '')) === -1){
			this.router.navigate(['posts']);
			return false;
		  }
		} else {
		  // Handle the case where info.role is undefined or empty
		  this.router.navigate(['']);
		  return false;
		}

		return true;
	  }

}
