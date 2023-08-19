import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/user/services/authentication.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) { }

  ngOnInit(): void {

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
