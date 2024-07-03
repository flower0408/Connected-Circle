import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Group } from '../model/group.model';
import {GroupRequest} from "../model/groupRequest.model";
import {Report} from "../../report/model/report.model";
import {User} from "../../user/model/user.model";

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  private headers = new HttpHeaders({'authorization': 'Bearer ' + JSON.parse(localStorage.user).accessToken,
  'Content-Type': 'application/json'});

  private headers2 = new HttpHeaders({'authorization': 'Bearer ' + JSON.parse(localStorage.user).accessToken});

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

  getMembersForGroup(groupId: number): Observable<HttpResponse<User[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/groups/members/' + groupId, queryParams) as Observable<HttpResponse<User[]>>;
  }

  getAdminsForGroup(groupId: number): Observable<HttpResponse<User[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/groups/admins/' + groupId, queryParams) as Observable<HttpResponse<User[]>>;
  }

  blockUser(memberId: number, adminId: number): Observable<any> {
    // Define the request body with both memberId and adminId
    const requestBody = { memberId: memberId, adminId: adminId };
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + JSON.parse(localStorage.user).accessToken,
      'Content-Type': 'application/json'
    });
    return this.http.post('api/groups/block-member', requestBody, {
      headers: headers,
      responseType: 'text'
    });
  }



  getAll(): Observable<HttpResponse<Group[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/groups', queryParams) as Observable<HttpResponse<Group[]>>;
  }

  private formatFormData(name: string, description: string, attachedPDF: File): FormData {
    const formData = new FormData();
    formData.append('name', name);
    formData.append('description', description);
    if (attachedPDF) {
      formData.append('attachedPDF', attachedPDF);
    }
    return formData;
  }
  
  add(name: string, description: string, attachedPDF: File): Observable<any> {
    const formData = this.formatFormData(name, description, attachedPDF);
  
    return this.http.post('api/groups/add', formData, {
      headers: this.headers2,
      responseType: 'text'
    });
  }

  /*add(newGroup: Group): Observable<string> {
    return this.http.post('api/groups/add', newGroup, {headers: this.headers, responseType: 'text'});
  }*/

  edit(editedGroup: Group): Observable<string> {
    return this.http.patch('api/groups/edit/' + editedGroup.id, editedGroup, {headers: this.headers, responseType: 'text'});
  }

  delete(id: number): Observable<HttpResponse<Group>> {
    return this.http.delete('api/groups/delete/' + id, {headers: this.headers}) as Observable<HttpResponse<Group>>;
  }

  addGroupAdmin(groupId: number, adminId: number): Observable<string> {
    return this.http.post('api/groups/' + groupId + '/admin/' + adminId, {}, {headers: this.headers, responseType: 'text'});
  }

  addAdminGroup(groupId: number, adminId: number): Observable<string> {
    return this.http.post('api/groups/' + groupId + '/adminGroup/' + adminId, {}, {headers: this.headers, responseType: 'text'});
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
