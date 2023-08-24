import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/user/services/authentication.service';
import {JwtHelperService} from "@auth0/angular-jwt";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  isAdmin: boolean = false;
  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.checkAdminStatus();
  }

  private checkAdminStatus() {
    const item = localStorage.getItem('user') || '';

    const jwt: JwtHelperService = new JwtHelperService();
    const decodedToken = jwt.decodeToken(item);
    const role = decodedToken.role.authority;

    this.isAdmin = role.includes('ADMIN');
  }
  logout() {
    this.authenticationService.logout().subscribe(
      result => {
        localStorage.removeItem('user');
        window.alert('Successful logout!');
        this.router.navigate(['users/login']).then(() => window.location.reload());
      },
      error => {
        window.alert('An error occurred!');
        console.log(error);
      }
    )
  }
}
