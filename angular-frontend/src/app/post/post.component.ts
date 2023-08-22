import { Component, OnInit } from '@angular/core';
import { Post } from './model/post.model';
import { Router } from '@angular/router';
import { Image } from './model/image.model';
import { User } from '../user/model/user.model';
import { PostService } from '../post/services/post.service';
import { UserService } from '../user/services/user.service';
import {JwtHelperService} from "@auth0/angular-jwt";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthenticationService} from "../user/services/authentication.service";

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {

  form: FormGroup;
  imagePath: string = '';
  image: Image = new Image();
  post: Post = new Post();
  user: User = new User();
  images: Image[] = [];

  constructor(
    private postService: PostService,
    private userService: UserService,
    private router: Router,
    private fb: FormBuilder,
    private authenticationService: AuthenticationService,

  ) {
    this.form = this.fb.group({
      image: [null],
    });
  }

  ngOnInit(): void {
    const item: string = localStorage.getItem('user') || '';
    const jwt: JwtHelperService = new JwtHelperService();
    const sub: string = jwt.decodeToken(item).sub;
    const url: String = this.router.url;
    const id: number = Number.parseInt(url.split('/')[2]);

    this.postService.getOne(id).subscribe(
      result => {
        this.post = result.body as Post;

        this.userService.getOne(this.post.postedByUserId).subscribe(
          result => {
            this.user = result.body as User;
            this.form.patchValue(this.user);

            this.userService.getProfileImage(this.user.id).subscribe(
              result => {
                if (result.body != null)
                  this.image = result.body as Image;
              },
              error => {
                window.alert('Error while retrieving profile image');
                console.log(error);
              }
            );
          },
          error => {
            window.alert('Error while retriving post\'s user');
            console.log(error);
          }
        );

        this.postService.getImages(this.post.id).subscribe(
          result => {
            this.images = result.body as Image[];
          },
          error => {
            window.alert('Error while retriving images for post');
            console.log(error);
          }
        );
      }
    );

    /*this.userService.getOneByUsername(sub).subscribe(
      result => {
        this.user = result.body as User;
        this.form.patchValue(this.user);

        this.userService.getProfileImage(this.user.id).subscribe(
          result => {
            if (result.body != null)
              this.image = result.body as Image;
          },
          error => {
            window.alert('Error while retrieving profile image');
            console.log(error);
          }
        );

      },
      error => {
        window.alert('Error while retrieving profile info');
        console.log(error);
      }
    );*/
  }



}
