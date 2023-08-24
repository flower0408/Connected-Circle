import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Group } from "src/app/group/model/group.model";
import { User } from 'src/app/user/model/user.model';
import { UserService } from 'src/app/user/services/user.service';
import { GroupService } from '../services/group.service';
import { JwtHelperService } from '@auth0/angular-jwt';

@Component({
  selector: 'app-edit-group',
  templateUrl: './edit-group.component.html',
  styleUrls: ['./edit-group.component.css']
})
export class EditGroupComponent implements OnInit {

  form: FormGroup;
  editing: boolean = this.router.url.includes('edit');

  group: Group = new Group();
  user: User = new User();

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private groupService: GroupService,
    private router: Router
  ) {
    this.form = this.fb.group({
      name: [null, Validators.required],
      description: [null, Validators.required]
    });

    if (this.editing) {
      const id: number = Number.parseInt(this.router.url.split('/')[3]);
      this.groupService.getOne(id).subscribe(
        result => {
          this.group = result.body as Group;
          this.form.patchValue(this.group);
        },
        error => {
          window.alert('An error occurred retriving group!');
          console.log(error);
        }
      );
    }
   }

   ngOnInit(): void {
    const jwt: JwtHelperService = new JwtHelperService();
    const userToken: string = localStorage.getItem('user') || '';
    const decoded = jwt.decodeToken(userToken);

    this.userService.getOneByUsername(decoded.sub).subscribe(
      result => {
        this.user = result.body as User;
      },
      error => {
        window.alert('Couln\'t find logged in user');
        console.log(error);
      }
    );
   }

   submit(): void {
      this.group.name = this.form.value.name;
      this.group.description = this.form.value.description;

      this.groupService.edit(this.group).subscribe(
        result => {
          window.alert('Successfully edited the group');
          this.router.navigate(['/groups/' + this.group.id]);
        },
        error => {
          window.alert('An error occurred editing the group');
          console.log(error);
        }
      );

  }
}
