import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
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

  private headers2 = new HttpHeaders({'authorization': 'Bearer ' + JSON.parse(localStorage.user).accessToken});

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

  getComments(id: number): Observable<HttpResponse<Comment[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/posts/' + id + '/comments', queryParams) as Observable<HttpResponse<Comment[]>>;
  }

  delete(id: number): Observable<HttpResponse<Post>> {
    return this.http.delete('api/posts/delete/' + id, {headers: this.headers}) as Observable<HttpResponse<Post>>;
  }

  private formateFormDataWithGroupId(
    title: string, 
    content: string, 
    postedByUserId: string, 
    belongsToGroupId: string, 
    attachedPDF: File
  ): FormData {
    let formData: FormData = new FormData();
    formData.append('title', title);
    formData.append('content', content);
    formData.append('postedByUserId', postedByUserId);
    formData.append('belongsToGroupId', belongsToGroupId);
    formData.append('attachedPDF', attachedPDF);
    return formData;
  }
  
  private formateFormDataWithoutGroupId(
    title: string, 
    content: string, 
    postedByUserId: string, 
    attachedPDF: File
  ): FormData {
    let formData: FormData = new FormData();
    formData.append('title', title);
    formData.append('content', content);
    formData.append('postedByUserId', postedByUserId);
    formData.append('attachedPDF', attachedPDF);
    return formData;
  }
  
  add(
    title: string, 
    content: string, 
    postedByUserId: string, 
    belongsToGroupId: string | null, 
    attachedPDF: File
  ): Observable<any> {
    let formData: FormData;
  
    if (belongsToGroupId !== null) {
      formData = this.formateFormDataWithGroupId(title, content, postedByUserId, belongsToGroupId, attachedPDF);
    } else {
      formData = this.formateFormDataWithoutGroupId(title, content, postedByUserId, attachedPDF);
    }
    
    return this.http.post('api/posts/add', formData, {
      headers: this.headers2,
      responseType: 'text'
    });
  }

  /*add(newPost: Post): Observable<string> {
    return this.http.post('api/posts/add', newPost, {headers: this.headers, responseType: 'text'});
  }*/

  edit(editedPost: Post): Observable<string> {
    return this.http.patch('api/posts/edit/' + editedPost.id, editedPost, {headers: this.headers, responseType: 'text'});
  }
  getAllForGroup(id: number): Observable<HttpResponse<Post[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/posts/group/' + id, queryParams) as Observable<HttpResponse<Post[]>>;
  }

  getAllForGroupAsc(id: number): Observable<HttpResponse<Post[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/posts/group/' + id + '/sort/asc', queryParams) as Observable<HttpResponse<Post[]>>;
  }


  getAllForGroupDesc(id: number): Observable<HttpResponse<Post[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/posts/group/' + id + '/sort/desc', queryParams) as Observable<HttpResponse<Post[]>>;
  }

  getSortedComments(id: number, order: string): Observable<HttpResponse<Comment[]>> {
    let queryParams = {};

    queryParams = {
      headers: this.headers,
      observe: 'response'
    };

    return this.http.get('api/posts/' + id + '/comments/sort/' + order, queryParams) as Observable<HttpResponse<Comment[]>>;
  }


}
