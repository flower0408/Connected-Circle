import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Post } from '../model/post.model';
import { Image } from '../model/image.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private headers = new HttpHeaders({'authorization': 'Bearer ' + JSON.parse(localStorage.user).accessToken,
  'Content-Type': 'application/json'});

  constructor(
    private http: HttpClient
  ) { }

  getHomepagePosts(): Observable<HttpResponse<Post[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/posts/homepage', queryParams) as Observable<HttpResponse<Post[]>>;
  }

  getHomepagePostsSorted(order: string): Observable<HttpResponse<Post[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/posts/homepage/sort/' + order, queryParams) as Observable<HttpResponse<Post[]>>;
  }

  getOne(id: number): Observable<HttpResponse<Post>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/posts/' + id, queryParams) as Observable<HttpResponse<Post>>;
  }

  getImages(id: number): Observable<HttpResponse<Image[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/posts/' + id + '/images', queryParams) as Observable<HttpResponse<Image[]>>;
  }
}
