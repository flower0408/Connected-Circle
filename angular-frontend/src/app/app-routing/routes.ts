import { Routes } from '@angular/router';

import { GroupRequestsComponent } from '../group/group-requests/group-requests.component';
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

export const routes :Routes = [
  {path: 'groups/:id/add-post', component: AddPostComponent},
  {path: 'groups/:id/group-requests', component: GroupRequestsComponent},
  {path: 'groups/:id', component: GroupComponent},
  {path: 'groups', component: GroupListComponent},
  {path: 'users/login', component: LoginComponent},
  {path: 'users/register', component: RegisterComponent},
  {path: 'users/profile', component: UserComponent},
  {path: 'users/change-password', component: UserComponent},
  {path: 'users/friends/requests', component: FriendRequestsComponent},
  {path: 'users/friends', component: FriendsComponent},
  {path: 'posts/add-new-post', component: AddPostComponent},
  {path: 'posts/edit/:id', component: EditPostComponent},
  {path: 'posts/:id', component: PostComponent},
  {path: 'posts', component: PostListComponent},
  {path: '', pathMatch: 'full', redirectTo: 'users/login', title: 'Connected Circle'}

];
