import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing/app-routing.module';
import { GroupListComponent } from './group/group-list/group-list.component';
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
import { HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './core/header/header.component';
import {NgOptimizedImage} from "@angular/common";

@NgModule({
  declarations: [
    AppComponent,
    GroupComponent,
    LoginComponent,
    GroupListComponent,
    UserComponent,
    RegisterComponent,
    PostComponent,
    NavbarComponent,
    HeaderComponent,
    FriendRequestsComponent,
    FriendsComponent,
    PostListComponent,
    EditPostComponent,
    AddPostComponent
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
