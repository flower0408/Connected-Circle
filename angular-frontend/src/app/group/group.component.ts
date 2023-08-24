import { Component, OnInit } from '@angular/core';
import { Group } from './model/group.model';
import {User} from "../user/model/user.model";
import {Post} from "../post/model/post.model";
import {GroupService} from "./services/group.service";
import {UserService} from "../user/services/user.service";
import {PostService} from "../post/services/post.service";
import {Router} from "@angular/router";
import {JwtHelperService} from "@auth0/angular-jwt";

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css']
})
export class GroupComponent implements OnInit{

  user: User = new User();

  group: Group = new Group();
  posts: Post[] = [];
  users: Map<number, User> = new Map();
  canPost: boolean = false;

  groupAdmins: User[] = [];

  constructor( private groupService: GroupService,
               private postService: PostService,
               private userService: UserService,
               private router: Router) {

  }

  ngOnInit(): void {
    const url: String = this.router.url;
    const id: number = Number.parseInt(url.split('/')[2]);

    this.groupService.getOne(id).subscribe(
      result => {
        this.group = result.body as Group;

        this.userService.getGroupAdmins(this.group.id).subscribe(
          result => {
            this.groupAdmins = result.body as User[];
          }
        );
      }
    );

    this.postService.getAllForGroup(id).subscribe(
      result => {
        this.posts = result.body as Post[];

        this.posts.forEach(post => {
          this.userService.getOne(post.postedByUserId).subscribe(
            result => {
              let user: User = result.body as User;
              this.users.set(user.id, user);
            }
          )
        });
      }
    );

    this.groupService.checkUserInGroup(id).subscribe(
      result => {
        const status = result.status;
        if (status == 200) {
          console.log(this.canPost);
        }
        this.canPost = true;
      }
    );

    let sub: string;
    const item = localStorage.getItem('user') || '';

    const jwt: JwtHelperService = new JwtHelperService();
    const decodedToken = jwt.decodeToken(item);
    sub = decodedToken.sub;

    this.userService.getOneByUsername(sub).subscribe(
      result => {
        this.user = result.body as User;
      }
    );
  }
  hasAuthority(): boolean {
    let role: string;
    const item = localStorage.getItem('user');

    if (!item) {
      this.router.navigate(['login']);
      role = "";
      return false;
    }

    const jwt: JwtHelperService = new JwtHelperService();
    const decodedToken = jwt.decodeToken(item);
    role = decodedToken.role.authority;

    if (role == "ROLE_ADMIN")
      return true;
    return false;
  }

  deleteGroup(): void {
    this.groupService.delete(this.group.id).subscribe(
      result => {
        window.alert('Successfully deleted group');
        this.router.navigate(['/groups']);
      },
      error => {
        window.alert('Error while deleting group');
        console.log(error);
      }
    );
  }

  suspendGroup(): void {
    const suspensionReason = prompt('Enter suspension reason');
    if (suspensionReason == null)
      return;

    this.group.suspendedReason = suspensionReason;
    this.group.suspended = true;

    this.groupService.edit(this.group).subscribe(
      result => {
        this.groupService.delete(this.group.id).subscribe(
          result => {
            window.alert('Group ' + this.group.name + ' suspended');
            this.router.navigate(['/groups']);
          },
          error => {
            window.alert('Error while suspending group ' + this.group.name);
            console.log(error);
          }
        );
      },
      error => {
        window.alert('Error while suspending group ' + this.group.name);
        console.log(error);
      }
    );
  }

  deleteGroupAdmin(): void {
    if (this.groupAdmins.length == 0) {
      window.alert('Group is unmoderated');
      return;
    }

    let promptText: string = '';
    this.groupAdmins.forEach(admin => {
      promptText += admin.id + ' -> ' + (admin.displayName || admin.username);
    });

    const adminId: number = prompt('Enter group admin id to be removed\n' + promptText) as unknown as number;
    if (adminId == null)
      return;

    let idExists: boolean = false;
    this.groupAdmins.forEach(admin => {
      if (admin.id == adminId)
        idExists = true;
    });

    if (!idExists) {
      window.alert('Wrong input');
      return;
    }

    this.groupService.deleteGroupAdmin(this.group.id, adminId).subscribe(
      result => {
        window.alert('Successfully deleted selected group admin');
        location.reload();
      },
      error => {
        window.alert('Error while deleting group admin');
        console.log(error);
      }
    );
  }

  isGroupAdmin(): boolean {
    let isAdmin: boolean = false;
    this.groupAdmins.forEach(groupAdmin => {
      if (groupAdmin.id == this.user.id)
        isAdmin = true;
    });
    return isAdmin;
  }
}
