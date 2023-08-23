import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Reaction } from '../model/reaction.model';

@Injectable({
  providedIn: 'root'
})
export class ReactionService {

  private headers = new HttpHeaders({'authorization': 'Bearer ' + JSON.parse(localStorage.user).accessToken,
  'Content-Type': 'application/json'});

  constructor(
    private http: HttpClient
  ) { }

  getOne(id: number): Observable<HttpResponse<Reaction>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/reactions/' + id, queryParams) as Observable<HttpResponse<Reaction>>;
  }

  getReactionsForPost(postId: number): Observable<HttpResponse<Reaction[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/reactions/post/' + postId, queryParams) as Observable<HttpResponse<Reaction[]>>;
  }

  getReactionsForComment(commentId: number): Observable<HttpResponse<Reaction[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/reactions/comment/' + commentId, queryParams) as Observable<HttpResponse<Reaction[]>>;
  }

  add(newReaction: Reaction): Observable<string> {
    return this.http.post('api/reactions/add', newReaction, {headers: this.headers, responseType: 'text'});
  }

  delete(id: number): Observable<HttpResponse<Reaction>> {
    return this.http.delete('api/reactions/delete/' + id, {headers: this.headers}) as Observable<HttpResponse<Reaction>>;
  }
}
