import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Report } from '../model/report.model';
import {Post} from "../../post/model/post.model";
import {Group} from "../../group/model/group.model";

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private headers = new HttpHeaders({'authorization': 'Bearer ' + JSON.parse(localStorage.user).accessToken,
    'Content-Type': 'application/json'});

  constructor(
    private http: HttpClient
  ) { }

  getOne(id: number): Observable<HttpResponse<Report>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/reports/' + id, queryParams) as Observable<HttpResponse<Report>>;
  }

  getAllReports(): Observable<HttpResponse<Report[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/reports/all', queryParams) as Observable<HttpResponse<Report[]>>;
  }

  add(newReport: Report): Observable<string> {
    console.log(newReport);
    return this.http.post('api/reports/add', newReport, {headers: this.headers, responseType: 'text'});
  }

  edit(editedReport: Report): Observable<string> {
    return this.http.patch('api/reports/edit/' + editedReport.id, editedReport, {headers: this.headers, responseType: 'text'});
  }

  editGroup(editedReport: Report): Observable<string> {
    return this.http.patch('api/reports/editForGroup/' + editedReport.id, editedReport, {headers: this.headers, responseType: 'text'});
  }


}
