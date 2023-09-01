
import { Component, OnInit } from '@angular/core';
import {GroupService} from "src/app/group/services/group.service";
import {Router} from "@angular/router";
import { ActivatedRoute } from '@angular/router';
import {User} from "../../user/model/user.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {JwtHelperService} from "@auth0/angular-jwt";
import {UserService} from "../../user/services/user.service";
@Component({
  selector: 'app-group-admins',
  templateUrl: './group-admins.component.html',
  styleUrls: ['./group-admins.component.css']
})
export class GroupAdminsComponent implements OnInit {
  admin:User = new User();
  admins: User[] = [];
  groupId!: number;
  user: User = new User();

  constructor(
    private fb: FormBuilder,
    private groupService: GroupService,
    private userService: UserService,
    private route: ActivatedRoute, // Inject ActivatedRoute
    private router: Router
  ) {
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


    this.route.params.subscribe(params => {
      const id = +params['id'];
      if (!isNaN(id)) {
        this.groupId = id; // Store the groupId
        this.groupService.getAdminsForGroup(id).subscribe(
          result => {
            this.admins = result.body as User[];
          }
        );
      } else {
        console.error('Invalid Group ID:', params['id']);
      }
    });
  }



}
