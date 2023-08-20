import { Injectable } from '@angular/core';
import {HttpHeaders, HttpClient, HttpResponse} from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserToken } from '../model/userToken.model';
import { Login } from '../model/login.model';
import { Register } from '../model/register.model';
import {User} from "../model/user.model";
import {ChangePassword} from "../model/changePassword.model";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient) {}

  login(auth: Login): Observable<HttpResponse<UserToken>> {
    return this.http.post('api/users/login', {username: auth.username, password: auth.password}, {headers: this.headers, responseType: 'json'}) as Observable<HttpResponse<UserToken>>;
  }

  logout(): Observable<string> {
    const authorizedHeaders = new HttpHeaders({'authorization': 'Bearer ' + JSON.parse(localStorage.user).accessToken, 'Content-Type': 'application/json'})
    return this.http.get('', {headers: authorizedHeaders, responseType: 'text'});
  }

  register(auth: Register): Observable<HttpResponse<User>> {
    return this.http.post('api/users/signup',
      {username: auth.username, password: auth.password, email: auth.email, firstName: auth.firstName, lastName: auth.lastName},
      {headers: this.headers, responseType: 'json'}) as Observable<HttpResponse<User>>;
  }

  changePassword(auth: ChangePassword): Observable<HttpResponse<User>> {
    const authorizedHeaders = new HttpHeaders({'authorization': 'Bearer ' + JSON.parse(localStorage.user).accessToken, 'Content-Type': 'application/json'})
    return this.http.post('api/users/change-password', {oldPassword: auth.oldPassword, newPassword: auth.newPassword}, {headers: authorizedHeaders, responseType: 'json'}) as Observable<HttpResponse<User>>;
  }


  isLoggedIn(): boolean {
    if (!localStorage.getItem('user')) {
      return false;
    }
    return true;
  }
}
