import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Router } from '@angular/router';

import { JwtHelperService } from '@auth0/angular-jwt';
import { Image } from 'src/app/post/model/image.model';
import { Group } from '../group/model/group.model';
import {UserService} from "./services/user.service";
import {User} from "./model/user.model";
import {AuthenticationService} from "./services/authentication.service";
import {ChangePassword} from "./model/changePassword.model";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  form: FormGroup;
  imagePath: string = '';
  isEditing: boolean = false;

  user: User = new User();
  image: Image = new Image();
  groups: Group[] = [];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authenticationService: AuthenticationService,
    private router: Router
  ) {
    this.form = this.fb.group({
      displayName: [null, Validators.minLength(3)],
      description: [null, Validators.maxLength(100)],
      image: [null, Validators.nullValidator],
      oldPassword: [null],
      newPassword1: [null],
      newPassword2: [null]
    });
  }

  ngOnInit(): void {
    const item: string = localStorage.getItem('user') || '';
    const jwt: JwtHelperService = new JwtHelperService();
    const sub: string = jwt.decodeToken(item).sub;

    this.userService.getOneByUsername(sub).subscribe(
      result => {
        this.user = result.body as User;
        this.form.patchValue(this.user);

        this.userService.getProfileImage(this.user.id).subscribe(
          result => {
            if (result.body != null)
              this.image = result.body as Image;
          },
          error => {
            window.alert('Error while retrieving profile image');
            console.log(error);
          }
        );

        this.userService.getUserGroups(this.user.id).subscribe(
          result => {
            this.groups = result.body as Group[];
          },
          error => {
            window.alert('Error while retriving groups user is member of');
            console.log(error);
          }
        );

      },
      error => {
        window.alert('Error while retrieving profile info');
        console.log(error);
      }
    );
  }

  onFileChange(event: Event): void {
    const fileInput = event.target as HTMLInputElement;
    const file = fileInput.files?.[0];
    if (file) {
      this.imagePath = file.name;
    }
  }


  submit(): void {
    const isPasswordEdited = this.form.value.oldPassword || this.form.value.newPassword1 || this.form.value.newPassword2;
    const arePasswordsMatching = this.form.value.newPassword1 === this.form.value.newPassword2;

    if (isPasswordEdited) {
      if (!arePasswordsMatching) {
        window.alert('Inputs for new password do not match!');
        return;
      }

      const auth: ChangePassword = new ChangePassword();
      auth.oldPassword = this.form.value.oldPassword;
      auth.newPassword = this.form.value.newPassword1;

      this.authenticationService.changePassword(auth).subscribe(
        result => {
          window.alert('Successfully changed password!');
          //localStorage.removeItem('user');
          this.router.navigate(['/users/profile']).then(() => window.location.reload());
        },
        error => {
          window.alert('Current password is incorrect!');
          console.log(error);
        }
      );
    } else {
      // Password is not edited

      if (this.form.dirty) {
        if (this.form.value.displayName) {
          this.user.displayName = this.form.value.displayName;
        }
        if (this.form.value.description) {
          this.user.description = this.form.value.description;
        }

        this.userService.updateUser(this.user).subscribe(
          result => {
            window.alert('Successfully edited your profile');
            this.router.navigate(['/users/profile']);
          },
          error => {
            window.alert('Error while editing your profile');
            console.log(error);
          }
        );
      }

      if (this.imagePath) {
        this.image.path = '../../assets/images/' + this.imagePath;
        this.image.belongsToUserId = this.user.id;
        this.user.profileImage = this.image;

        this.userService.updateUser(this.user).subscribe(
          result => {
            if (!this.form.dirty) { // If only the profile image was updated
              window.alert('Successfully edited your profile');
            }
            this.router.navigate(['/users/profile']);
          },
          error => {
            window.alert('Error while editing your profile');
            console.log(error);
          }
        );
      }
    }
  }
}





