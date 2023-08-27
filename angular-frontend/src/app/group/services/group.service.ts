import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Group } from '../model/group.model';
import {GroupRequest} from "../model/groupRequest.model";
import {Report} from "../../report/model/report.model";

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  private headers = new HttpHeaders({'authorization': 'Bearer ' + JSON.parse(localStorage.user).accessToken,
  'Content-Type': 'application/json'});

  constructor(
    private http: HttpClient
  ) { }

  getOne(id: number): Observable<HttpResponse<Group>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/groups/' + id, queryParams) as Observable<HttpResponse<Group>>;
  }

  getReportsForGroup(groupId: number): Observable<HttpResponse<Report[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/groups/reports/' + groupId, queryParams) as Observable<HttpResponse<Report[]>>;
  }

  getAll(): Observable<HttpResponse<Group[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/groups', queryParams) as Observable<HttpResponse<Group[]>>;
  }

  add(newGroup: Group): Observable<string> {
    return this.http.post('api/groups/add', newGroup, {headers: this.headers, responseType: 'text'});
  }

  edit(editedGroup: Group): Observable<string> {
    return this.http.patch('api/groups/edit/' + editedGroup.id, editedGroup, {headers: this.headers, responseType: 'text'});
  }

  delete(id: number): Observable<HttpResponse<Group>> {
    return this.http.delete('api/groups/delete/' + id, {headers: this.headers}) as Observable<HttpResponse<Group>>;
  }

  addGroupAdmin(groupId: number, adminId: number): Observable<string> {
    return this.http.post('api/groups/' + groupId + '/admin/' + adminId, {}, {headers: this.headers, responseType: 'text'});
  }

  deleteGroupAdmin(groupId: number, adminId: number): Observable<HttpResponse<Group>> {
    return this.http.delete('api/groups/delete/' + groupId + '/admin/' + adminId, {headers: this.headers}) as Observable<HttpResponse<Group>>;
  }

  checkUserInGroup(id:number): Observable<HttpResponse<boolean>> {
    return this.http.get('api/posts/group/'+ id + '/user', {headers: this.headers}) as Observable<HttpResponse<boolean>>;
  }

  getGroupRequests(groupId: number): Observable<HttpResponse<GroupRequest[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/groups/' + groupId + '/group-requests', queryParams) as Observable<HttpResponse<GroupRequest[]>>;
  }

  sendGroupRequest(groupRequest: GroupRequest): Observable<HttpResponse<GroupRequest>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.post('api/groups/' + groupRequest.forGroupId + '/group-request', groupRequest, queryParams) as Observable<HttpResponse<GroupRequest>>;
  }

  updateGroupRequest(groupRequest: GroupRequest): Observable<string> {
    return this.http.patch('api/groups/group-request', groupRequest, {headers: this.headers, responseType: 'text'});
  }

  deleteGroupRequest(id: number): Observable<HttpResponse<Number>> {
    return this.http.delete('api/groups/group-request/' + id, {headers: this.headers}) as Observable<HttpResponse<Number>>;
  }

}
