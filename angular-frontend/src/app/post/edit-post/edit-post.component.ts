import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Post } from '../model/post.model';
import { PostService } from '../services/post.service';
import { UserService } from 'src/app/user/services/user.service';
import { Image } from '../model/image.model';

@Component({
  selector: 'app-add-edit-post',
  templateUrl: './edit-post.component.html',
  styleUrls: ['./edit-post.component.css']
})
export class EditPostComponent implements OnInit{

  form: FormGroup;
  editing: boolean = this.router.url.includes('edit');
  imagePaths: string[] = [];
  images: Image[] = [];

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private postService: PostService,
  ) {
    console.log('Current URL:', this.router.url);
    this.form = this.fb.group({
      content: [null, Validators.required],
      images: [null, Validators.nullValidator]
    });

    if (this.editing) {
      const urlSegments = this.router.url.split('/');
      if (urlSegments.length >= 4) {
        const id: number = Number.parseInt(urlSegments[3]);
        console.log('Extracted id:', id);
        this.postService.getOne(id).subscribe(
          result => {
            const post: Post = result.body as Post;
            this.form.patchValue(post);
          },
          error => {
            window.alert('An error occurred retriving post!');
            console.log(error);
          }
        );
      }
    }
  }
  ngOnInit(): void {

  }

  onFileChange(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    const files: FileList | null = inputElement.files;
    this.imagePaths = [];

    if (files) {
      for (let i = 0; i < files.length; i++) {
        const file: File = files[i];
        this.imagePaths.push(file.name);
      }
    }
  }

  submit() {

      const post: Post = new Post({_id: Number.parseInt(this.router.url.split('/')[3])});
      post.content = this.form.value.content;

      if (this.imagePaths.length > 0) {
        this.imagePaths.forEach((imageName: string) => {
          let image: Image = new Image();
          image.path = '../../assets/images/' + imageName;
          image.belongsToPostId = post.id;
          this.images.push(image);
        });
        post.images = this.images;
      }

      this.postService.edit(post).subscribe(
        result => {
          window.alert('Successfully edited the post!');
          this.router.navigate(['posts']);
        },
        error => {
          window.alert('An error occurred editing the post!');
          console.log(error);
        }
      );
    }

}
