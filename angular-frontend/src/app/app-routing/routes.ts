import { Routes } from '@angular/router';

import { GroupListComponent } from '../group/group-list/group-list.component';
import { LoginComponent } from '../user/login/login.component';
import { RegisterComponent } from '../user/register/register.component';
import { UserComponent } from '../user/user.component';
import { PostComponent } from '../post/post.component';

export const routes :Routes = [
  {path: 'groups', component: GroupListComponent},
  {path: 'users/login', component: LoginComponent},
  {path: 'users/register', component: RegisterComponent},
  {path: 'users/profile', component: UserComponent, title: 'Social Network'},
  {path: '', pathMatch: 'full', redirectTo: 'users/login', title: 'Connected Circle'}

];
