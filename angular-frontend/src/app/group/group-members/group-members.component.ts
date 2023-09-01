import { Component, OnInit } from '@angular/core';
import { Report } from 'src/app/report/model/report.model';
import {GroupService} from "src/app/group/services/group.service";
import {Router} from "@angular/router";
import { ActivatedRoute } from '@angular/router';
import {User} from "../../user/model/user.model";
import {Banned} from "../../banned/model/banned.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {JwtHelperService} from "@auth0/angular-jwt";
import {UserService} from "../../user/services/user.service";
@Component({
  selector: 'app-group-members',
  templateUrl: './group-members.component.html',
  styleUrls: ['./group-members.component.css']
})
export class GroupMembersComponent implements OnInit {
  form: FormGroup;
  member:User = new User();
  members: User[] = [];
  groupId!: number;
  user: User = new User();

  constructor(
    private fb: FormBuilder,
    private groupService: GroupService,
    private userService: UserService,
    private route: ActivatedRoute, // Inject ActivatedRoute
    private router: Router
  ) {
    this.form = this.fb.group({
      accepted: [null, Validators.required]
    });
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
        this.groupService.getMembersForGroup(id).subscribe(
          result => {
            this.members = result.body as User[];
          }
        );
      } else {
        console.error('Invalid Group ID:', params['id']);
      }
    });
  }

  blockUser(memberId: number): void {
    const adminId = this.user.id;
    // Use this.groupId to pass the groupId to the blockUser function
    this.groupService.blockUser(memberId, adminId).subscribe(
      response => {
        window.alert('Successfully blocked member');
        location.reload();
        console.log('User blocked successfully');
      },
      error => {
        window.alert('Error while blocking member');
        console.error('Error blocking user:', error);
      }
    );
  }


  /*
    submit(): void {
      this.member.user = this.form.value.accepted;

      this.reportService.editGroup(this.report).subscribe(
        result => {
          window.alert('Successfully edited the report');
          this.location.back();
        },
        error => {
          window.alert('An error occurred editing the report');
          console.log(error);
        }
      );

    }*/

  /*submitBanned(banned: Banned): void {
    this.bannedService.unblockMember(banned.id).subscribe(
      result => {
        window.alert('Successfully edited the banned');
        this.location.back();
      },
      error => {
        window.alert('An error occurred editing the banned');
        console.log(error);
      }
    );
  }*/

}
