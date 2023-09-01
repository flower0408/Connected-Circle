import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing/app-routing.module';
import { BannedListComponent } from './banned/banned-list.component';
import { GroupListComponent } from './group/group-list/group-list.component';
import { AddGroupComponent } from './group/add-group/add-group.component';
import { EditGroupComponent } from './group/edit-group/edit-group.component';
import { GroupRequestsComponent } from './group/group-requests/group-requests.component';
import { GroupReportsComponent } from './group/group-reports/group-reports.component';
import { GroupComponent } from './group/group.component';
import { LoginComponent } from './user/login/login.component';
import { UserComponent } from './user/user.component';
import { RegisterComponent } from './user/register/register.component';
import { PostComponent } from './post/post.component';
import { NavbarComponent } from './core/navbar/navbar.component';
import { FriendRequestsComponent } from './user/friend-requests/friend-requests.component';
import { FriendsComponent } from './user/friends/friends.component';
import { PostListComponent } from './post/post-list/post-list.component';
import { EditPostComponent } from './post/edit-post/edit-post.component';
import { AddPostComponent } from './post/add-post/add-post.component';
import { ReportComponent } from './report/report.component';
import { ReportListComponent } from './report/report-list/report-list.component';
import { EditReportComponent } from './report/edit-report/edit-report.component';
import { EditReportGroupComponent } from './group/edit-report-group/edit-report-group.component';
import { HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './core/header/header.component';
import {NgOptimizedImage} from "@angular/common";
import {GroupMembersComponent} from "./group/group-members/group-members.component";
import {BannedListGroupComponent} from "./banned/banned-list-group/banned-list-group.component";
import {GroupAdminsComponent} from "./group/group-admins/group-admins.component";

@NgModule({
  declarations: [
    AppComponent,
    GroupComponent,
    GroupRequestsComponent,
    AddGroupComponent,
    EditGroupComponent,
    LoginComponent,
    GroupListComponent,
    GroupReportsComponent,
    UserComponent,
    RegisterComponent,
    PostComponent,
    NavbarComponent,
    HeaderComponent,
    FriendRequestsComponent,
    FriendsComponent,
    PostListComponent,
    EditPostComponent,
    AddPostComponent,
    ReportComponent,
    ReportListComponent,
    EditReportComponent,
    EditReportGroupComponent,
    BannedListComponent,
    GroupMembersComponent,
    BannedListGroupComponent,
    GroupAdminsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgOptimizedImage
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
