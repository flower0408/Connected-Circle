import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Router } from '@angular/router';
import { Post } from '../model/post.model';
import { PostService } from '../services/post.service';
import { UserService } from 'src/app/user/services/user.service';
import { User } from 'src/app/user/model/user.model';
import { Image } from '../model/image.model';

@Component({
  selector: 'app-add-post',
  templateUrl: './add-post.component.html',
  styleUrls: ['./add-post.component.css']
})
export class AddPostComponent implements OnInit{

  form: FormGroup;
  postForGroup: number = Number.parseInt(this.router.url.split('/')[2]) || 0;
  imagePaths: string[] = [];
  images: Image[] = [];

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private postService: PostService,
    private userService: UserService
  ) {
    console.log('Current URL:', this.router.url);
    this.form = this.fb.group({
      title: ['', [Validators.required]],
      content: [null, Validators.required],
      images: [null, Validators.nullValidator],
      attachedPDF: ['', Validators.required],
    });

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

  onPDFFileChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.form.patchValue({
        attachedPDF: file
      });
      this.form.get('attachedPDF')?.updateValueAndValidity();
    }
  }

  submit() {
    const post: Post = new Post();
    post.content = this.form.value.content;
    post.creationDate = new Date().toISOString().slice(0, -1);

    const title = this.form.get('title');
    const content = this.form.get('content');
    const attachedPDF = this.form.get('attachedPDF');
    const jwt: JwtHelperService = new JwtHelperService();
    const userToken: string = localStorage.getItem('user') || '';
    const decoded = jwt.decodeToken(userToken);

    this.userService.getOneByUsername(decoded.sub).subscribe(
      result => {
        let user: User = result.body as User;
        post.postedByUserId = user.id;
        const postedByUserId = post.postedByUserId.toString();

        if (this.imagePaths.length > 0) {
          this.imagePaths.forEach((imageName: string) => {
            let image: Image = new Image();
            image.path = '../../assets/images/' + imageName;
            image.belongsToPostId = post.id;
            this.images.push(image);
          });
        }

        post.images = this.images;
        let belongsToGroupId: string | null = null; 

      if (this.postForGroup > 0) {
        post.belongsToGroupId = this.postForGroup;
        belongsToGroupId = post.belongsToGroupId.toString();
      }

        this.postService.add(title?.value, content?.value,postedByUserId, belongsToGroupId , attachedPDF?.value).subscribe(
          result => {
            window.alert('Successfully added a post!');
             if (this.postForGroup > 0)
              this.router.navigate(['groups/' + this.postForGroup]);
              else
            this.router.navigate(['posts']);
          },
          error => {
            window.alert('An error occurred adding a post!');
            console.log(error);
          }
        );
      }
    );

}
}
