import { Routes } from '@angular/router';

import { GroupListComponent } from '../group/group-list/group-list.component';
import { LoginComponent } from '../user/login/login.component';
import { RegisterComponent } from '../user/register/register.component';
import { UserComponent } from '../user/user.component';
import { PostComponent } from '../post/post.component';
import { FriendRequestsComponent } from '../user/friend-requests/friend-requests.component';
import { FriendsComponent } from '../user/friends/friends.component';

export const routes :Routes = [
  {path: 'groups', component: GroupListComponent},
  {path: 'users/login', component: LoginComponent},
  {path: 'users/register', component: RegisterComponent},
  {path: 'users/profile', component: UserComponent},
  {path: 'users/change-password', component: UserComponent},
  {path: 'users/friends/requests', component: FriendRequestsComponent},
  {path: 'users/friends', component: FriendsComponent},
  {path: '', pathMatch: 'full', redirectTo: 'users/login', title: 'Connected Circle'}

];
