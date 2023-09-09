import {Component, OnInit} from '@angular/core';
import { Group } from '../model/group.model';
import {User} from "../../user/model/user.model";
import {GroupService} from "../services/group.service";
import {UserService} from "../../user/services/user.service";
import {JwtHelperService} from "@auth0/angular-jwt";
import {GroupRequest} from "../model/groupRequest.model";
import {Banned} from "../../banned/model/banned.model";
import {BannedService} from "../../banned/services/banned.service";

@Component({
  selector: 'app-group-list',
  templateUrl: './group-list.component.html',
  styleUrls: ['./group-list.component.css']
})
export class GroupListComponent implements OnInit {
  groups: Group[] = [];
  user: User = new User();
  isAdmin: boolean = false;
  userGroups: Group[] = [];
  groupRequests: Map<number, GroupRequest[]> = new Map();
  bannedUsersForGroup: Map<number, Banned[]> = new Map();
  bans: Banned[] = [];

  constructor(private groupService: GroupService,
              private userService: UserService,
              private  bannedService: BannedService) {

  }

  ngOnInit(): void {
    this.groupService.getAll().subscribe(
      result => {
        this.groups = result.body as Group[];

        this.groups.forEach(group => {
          this.groupService.getGroupRequests(group.id).subscribe(
            result => {
              this.groupRequests.set(group.id, result.body as GroupRequest[]);
            }
          );
          this.bannedService.getAllGroup(group.id).subscribe(
            result => {
              this.bannedUsersForGroup.set(group.id, result.body as Banned[]);

            }
          );
        });
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

  canSendRequest(groupId: number): boolean {
    let canSend: boolean = true;
    const isUserBanned = this.isUserBannedForGroup(groupId);


    this.groupRequests.forEach(requests => {
      requests.forEach(request => {
        if (request.createdByUserId == this.user.id && request.forGroupId == groupId)
          canSend = false;
      });
    });

    if (isUserBanned || this.canAccess(groupId)) {
      canSend = false;
    }
    return canSend;
  }

  private isUserBannedForGroup(groupId: number): boolean {
    const bannedUsers = this.bannedUsersForGroup.get(groupId);
    return bannedUsers?.some(banned => banned.towardsUserId === this.user.id) ?? false;
  }

  sendRequest(groupId: number): void {
    const groupRequest: GroupRequest = new GroupRequest();
    groupRequest.createdAt = new Date().toISOString().slice(0, -1);
    groupRequest.createdByUserId = this.user.id;
    groupRequest.forGroupId = groupId;

    this.groupService.sendGroupRequest(groupRequest).subscribe(
      result => {
        window.alert('Successfully sent request to join group');
        location.reload();
      },
      error => {
        window.alert('Error while sending group request');
        console.log(error);
      }
    );
  }
}
