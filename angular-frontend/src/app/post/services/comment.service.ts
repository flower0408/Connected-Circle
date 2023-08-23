import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comment } from '../model/comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private headers = new HttpHeaders({'authorization': 'Bearer ' + JSON.parse(localStorage.user).accessToken,
  'Content-Type': 'application/json'});

  constructor(
    private http: HttpClient
  ) { }

  getOne(id: number): Observable<HttpResponse<Comment>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/comments/' + id, queryParams) as Observable<HttpResponse<Comment>>;
  }

  add(newComment: Comment): Observable<string> {
    return this.http.post('api/comments/add', newComment, {headers: this.headers, responseType: 'text'});
  }

  edit(editedComment: Comment): Observable<string> {
    return this.http.patch('api/comments/edit/' + editedComment.id, editedComment, {headers: this.headers, responseType: 'text'});
  }

  delete(id: number): Observable<HttpResponse<Comment>> {
    return this.http.delete('api/comments/delete/' + id, {headers: this.headers}) as Observable<HttpResponse<Comment>>;
  }
}
