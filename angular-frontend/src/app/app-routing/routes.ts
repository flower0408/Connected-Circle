import { Routes } from '@angular/router';

import { GroupListComponent } from '../group/group-list/group-list.component';
import { LoginComponent } from '../user/login/login.component';
import { RegisterComponent } from '../user/register/register.component';

export const routes :Routes = [
  {path: 'groups', component: GroupListComponent},
  {path: 'users/login', component: LoginComponent},
  {path: 'users/register', component: RegisterComponent}

];
