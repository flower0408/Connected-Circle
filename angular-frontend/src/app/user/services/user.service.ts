import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable } from 'rxjs';
import { User } from '../model/user.model';
import { Image } from 'src/app/post/model/image.model';
import { Group } from 'src/app/group/model/group.model';
import { UserSearch } from '../model/userSearch.model';
import {FriendRequest} from "../model/friendRequest.model";


@Injectable({
  providedIn: 'root'
})
export class UserService {

  private headers = new HttpHeaders({'authorization': 'Bearer ' + JSON.parse(localStorage.user).accessToken,
  'Content-Type': 'application/json'});

  constructor(
    private http: HttpClient
  ) { }

  getOne(id: number): Observable<HttpResponse<User>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/users/' + id, queryParams) as Observable<HttpResponse<User>>;
  }

  getOneByUsername(username: string): Observable<HttpResponse<User>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/users/user/' + username, queryParams) as Observable<HttpResponse<User>>;
  }

  getProfileImage(id: number): Observable<HttpResponse<Image>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/users/' + id + '/image', queryParams) as Observable<HttpResponse<Image>>;
  }

  updateUser(user: User): Observable<string> {
    return this.http.patch('api/users/edit/', user, {headers: this.headers, responseType: 'text'});
  }

  getFriendRequests(): Observable<HttpResponse<FriendRequest[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/users/friend-request', queryParams) as Observable<HttpResponse<FriendRequest[]>>;
  }

  updateFriendRequest(request: FriendRequest): Observable<string> {
    return this.http.patch('api/users/friend-request', request, {headers: this.headers, responseType: 'text'});
  }

  deleteFriendRequest(id: number): Observable<HttpResponse<Number>> {
    return this.http.delete('api/users/friend-request/' + id, {headers: this.headers}) as Observable<HttpResponse<Number>>;
  }

  getUserFriends(): Observable<HttpResponse<User[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/users/friends', queryParams) as Observable<HttpResponse<User[]>>;
  }

  searchUsers(query: UserSearch): Observable<HttpResponse<User[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.post('api/users/search', query, queryParams) as Observable<HttpResponse<User[]>>;
  }

  sendFriendRequest(friendRequest: FriendRequest): Observable<HttpResponse<boolean>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.post('api/users/' + friendRequest.toUserId + '/friend-request', friendRequest, queryParams) as Observable<HttpResponse<boolean>>;
  }

  extractUser(): Promise<User> {
    let sub: string;
    const item = localStorage.getItem('user') || "";
    const jwt: JwtHelperService = new JwtHelperService();
    const decodedToken = jwt.decodeToken(item);
    sub = decodedToken.sub;

    return new Promise<User>((resolve, reject) => {
      this.getOneByUsername(sub).subscribe(
        result => {
          const user = result.body as User;
          resolve(user);
        },
        error => {
          reject(error);
        }
      );
    });
  }
  getUserGroups(userId: number): Observable<HttpResponse<Group[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/users/' + userId + '/groups', queryParams) as Observable<HttpResponse<Group[]>>;
  }

  getGroupAdmins(groupId: number): Observable<HttpResponse<User[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/users/group/' + groupId + '/admins', queryParams) as Observable<HttpResponse<User[]>>;
  }
}
