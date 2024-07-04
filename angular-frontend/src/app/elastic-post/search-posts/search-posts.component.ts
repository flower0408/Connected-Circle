import { Component, OnInit } from '@angular/core';
import { PostService } from 'src/app/post/services/post.service';
import PostElastic from "src/app/elastic-post/model/elasticPost.model"
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-search-posts',
  templateUrl: './search-posts.component.html',
  styleUrls: ['./search-posts.component.css']
})
export class SearchPostsComponent implements OnInit {
    searchForm: FormGroup;
    commentsRangeForm: FormGroup;
    likesRangeForm: FormGroup;
    posts: PostElastic[] = [];
  
    constructor(private postService: PostService, private fb: FormBuilder) {
      this.searchForm = this.fb.group({
        title: [''],
        content: [''],
        pdfContent: [''],
        operation: ['OR'],
        usePhraseQueryTitle: [false],
        useFuzzyQueryTitle: [false],
        usePhraseQueryContent: [false],
        useFuzzyQueryContent: [false],
        usePhraseQueryPDFContent: [false],
        useFuzzyQueryPDFContent: [false],
        usePhraseQuery: [false],
        useFuzzyQuery: [false]
      });
  
      this.commentsRangeForm = this.fb.group({
        greaterThan: [''],
        lessThan: ['']
      });
  
      this.likesRangeForm = this.fb.group({
        greaterThan: [''],
        lessThan: ['']
      });
    }
  
    ngOnInit(): void {}
  
    searchByTitle() {
      const { title, usePhraseQueryTitle, useFuzzyQueryTitle } = this.searchForm.value;
      this.postService.getElasticPostsByTitle(title, usePhraseQueryTitle, useFuzzyQueryTitle).subscribe(posts => {
        this.posts = posts;
      });
    }
  
    searchByContent() {
      const { content, usePhraseQueryContent, useFuzzyQueryContent } = this.searchForm.value;
      this.postService.getElasticPostsByContent(content, usePhraseQueryContent, useFuzzyQueryContent).subscribe(posts => {
        this.posts = posts;
      });
    }
  
    searchByPDFContent() {
      const { pdfContent, usePhraseQueryPDFContent, useFuzzyQueryPDFContent } = this.searchForm.value;
      this.postService.getElasticPostsByPDFContent(pdfContent, usePhraseQueryPDFContent, useFuzzyQueryPDFContent).subscribe(posts => {
        this.posts = posts;
      });
    }
  
    search() {
      const { title, content, pdfContent, operation, usePhraseQuery, useFuzzyQuery } = this.searchForm.value;
      this.postService.searchElasticPosts(title, content, pdfContent, operation, usePhraseQuery, useFuzzyQuery).subscribe(posts => {
        this.posts = posts;
      });
    }
  
    searchByCommentsRange() {
      const criteria = this.commentsRangeForm.value;
      this.postService.getElasticPostsByCommentsRange(criteria).subscribe(posts => {
        this.posts = posts;
      });
    }
  
    searchByLikesRange() {
      const criteria = this.likesRangeForm.value;
      this.postService.getElasticPostsByLikes(criteria).subscribe(posts => {
        this.posts = posts;
      });
    }
  }
  