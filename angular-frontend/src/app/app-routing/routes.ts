import { Routes } from '@angular/router';

import { ReportComponent } from '../report/report.component';
import { EditReportComponent } from '../report/edit-report/edit-report.component';
import { ReportListComponent } from '../report/report-list/report-list.component';
import {EditGroupComponent} from "../group/edit-group/edit-group.component";
import { AddGroupComponent } from '../group/add-group/add-group.component';
import { GroupRequestsComponent } from '../group/group-requests/group-requests.component';
import { GroupReportsComponent } from '../group/group-reports/group-reports.component';
import { EditReportGroupComponent } from '../group/edit-report-group/edit-report-group.component';
import { GroupListComponent } from '../group/group-list/group-list.component';
import { GroupComponent } from '../group/group.component';
import { LoginComponent } from '../user/login/login.component';
import { RegisterComponent } from '../user/register/register.component';
import { UserComponent } from '../user/user.component';
import { PostComponent } from '../post/post.component';
import { EditPostComponent } from '../post/edit-post/edit-post.component';
import { AddPostComponent } from '../post/add-post/add-post.component';
import { FriendRequestsComponent } from '../user/friend-requests/friend-requests.component';
import { FriendsComponent } from '../user/friends/friends.component';
import { PostListComponent } from '../post/post-list/post-list.component';
import {BannedListComponent} from "../banned/banned-list.component";
import {GroupMembersComponent} from "../group/group-members/group-members.component";
import {BannedListGroupComponent} from "../banned/banned-list-group/banned-list-group.component";
import {GroupAdminsComponent} from "../group/group-admins/group-admins.component";
import {LoginGuardService} from "../guards/login-guard.service";
import {RoleGuardService} from "../guards/role-guard.service";

export const routes :Routes = [
  {path: 'bans', component: BannedListComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN'}},
  {path: 'bansGroup/:groupId', component: BannedListGroupComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'groups/:id/add-post', component: AddPostComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'groups/:id/group-requests', component: GroupRequestsComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'groups/add', component: AddGroupComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'groups/edit/:id', component: EditGroupComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'groups/reports/:id', component: GroupReportsComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'groups/members/:id', component: GroupMembersComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'groups/admins/:id', component: GroupAdminsComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'groups/reports/editGroup/:id', component: EditReportGroupComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'groups/:id', component: GroupComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'groups', component: GroupListComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'reports/edit/:id', component: EditReportComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN'}},
  {path: 'reports/:id', component: ReportComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN'}},
  {path: 'reports', component: ReportListComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN'}},
  {path: 'users/login', component: LoginComponent, canActivate: [LoginGuardService]},
  {path: 'users/register', component: RegisterComponent},
  {path: 'users/profile', component: UserComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'users/change-password', component: UserComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'users/friends/requests', component: FriendRequestsComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'users/friends', component: FriendsComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'posts/add-new-post', component: AddPostComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'posts/edit/:id', component: EditPostComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'posts/:id', component: PostComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: 'posts', component: PostListComponent,
    canActivate: [RoleGuardService],
    data: {expectedRoles: 'ADMIN|USER'}},
  {path: '', pathMatch: 'full', redirectTo: 'users/login', title: 'Connected Circle'}

];
