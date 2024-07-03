import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Group } from "src/app/group/model/group.model";
import { User } from 'src/app/user/model/user.model';
import { UserService } from 'src/app/user/services/user.service';
import { GroupService } from '../services/group.service';
import { JwtHelperService } from '@auth0/angular-jwt';

@Component({
  selector: 'app-add-group',
  templateUrl: './add-group.component.html',
  styleUrls: ['./add-group.component.css']
})
export class AddGroupComponent implements OnInit {

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
      description: [null, Validators.required],
      attachedPDF: [''],
    });

   }

   onFileChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.form.patchValue({
        attachedPDF: file
      });
      this.form.get('attachedPDF')?.updateValueAndValidity();
    }
    console.log(this.form.get('attachedPDF')?.value);
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
      let group: Group = new Group();
      group.name = this.form.value.name;
      group.description = this.form.value.description;
      group.creationDate = new Date().toISOString().slice(0, -1);
      group.suspended = false;
      const attachedPDF = this.form.get('attachedPDF')?.value;
      const name = this.form.get('name');
      const description = this.form.get('description');

      this.groupService.add(name?.value, description?.value, attachedPDF).subscribe(
        result => {
          group = (JSON.parse(result)) as Group;

          this.groupService.addGroupAdmin(group.id, this.user.id).subscribe(
            result => {
              window.alert('Successfully created a group');
              this.router.navigate(['/groups']);
            },
            error => {
              window.alert('Error while adding group\'s admin');
              console.log(error);
            }
          );
        },
        error => {
          window.alert('Error while creating group');
          console.log(error);
        }
      );
  }
}
