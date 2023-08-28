import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Banned } from '../model/banned.model';

@Injectable({
  providedIn: 'root'
})
export class BannedService {

  private headers = new HttpHeaders({'authorization': 'Bearer ' + JSON.parse(localStorage.user).accessToken,
    'Content-Type': 'application/json'});

  constructor(
    private http: HttpClient
  ) { }

  getOne(id: number): Observable<HttpResponse<Banned>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/banned/' + id, queryParams) as Observable<HttpResponse<Banned>>;
  }

  getAll(): Observable<HttpResponse<Banned[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/banned', queryParams) as Observable<HttpResponse<Banned[]>>;
  }

  unblock(bannedId: number): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + JSON.parse(localStorage.user).accessToken,
      'Content-Type': 'application/json'
    });
    return this.http.patch('api/banned/unblock/' + bannedId, {}, {
      headers: headers,
      responseType: 'text'
    });
  }



}
