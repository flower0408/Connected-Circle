import {Component, OnInit} from '@angular/core';
import { Group } from '../model/group.model';
import {User} from "../../user/model/user.model";
import {GroupService} from "../services/group.service";
import {UserService} from "../../user/services/user.service";
import {JwtHelperService} from "@auth0/angular-jwt";

@Component({
  selector: 'app-group-list',
  templateUrl: './group-list.component.html',
  styleUrls: ['./group-list.component.css']
})
export class GroupListComponent implements OnInit {
  groups: Group[] = [];
  user: User = new User();
  isAdmin: boolean = false;
  userGroups: Group[] = []; //grupe kojih je ulogovani korisnik clan


  constructor(private groupService: GroupService,
              private userService: UserService) {

  }

  ngOnInit(): void {
    this.groupService.getAll().subscribe(
      result => {
        this.groups = result.body as Group[];

      }
    );

    let sub, role: string;
    const item = localStorage.getItem('user') || '';

    const jwt: JwtHelperService = new JwtHelperService();
    const decodedToken = jwt.decodeToken(item);
    sub = decodedToken.sub;
    role = decodedToken.role.authority;

    if (role.includes('ADMIN'))
      this.isAdmin = true;

    this.userService.getOneByUsername(sub).subscribe(
      result => {
        this.user = result.body as User;

        this.userService.getUserGroups(this.user.id).subscribe(
          result => {
            this.userGroups = result.body as Group[];
          }
        );
      }
    );
  }
  canAccess(groupId: number): boolean {
    let access: boolean = false;

    this.userGroups.forEach(group => {
      if (group.id == groupId)
        access = true;
    });

    return access;
  }
}
