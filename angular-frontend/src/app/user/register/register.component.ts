import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthenticationService } from '../services/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authenticationService: AuthenticationService,
    private router: Router
  ) {
    this.form = this.fb.group({
      username: [null, Validators.required],
      password: [null, Validators.required],
      email: [null, Validators.email],
      firstName: [null, Validators.required],
      lastName: [null, Validators.required]
    });
  }

  ngOnInit(): void {

  }

  submit() {
    const auth: any = {};
    auth.username = this.form.value.username;
    auth.password = this.form.value.password;
    auth.email = this.form.value.email;
    auth.firstName = this.form.value.firstName;
    auth.lastName = this.form.value.lastName;

    this.authenticationService.register(auth).subscribe(
      result => {
        window.alert('Successful registration!');
        console.log(result);
        this.router.navigate(['users/login']);
      },
      error => {
        console.log(error);
      }
    );
  }

}
