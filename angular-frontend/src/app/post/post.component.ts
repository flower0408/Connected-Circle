import { Component, OnInit } from '@angular/core';
import { Post } from './model/post.model';
import { Router } from '@angular/router';
import { Image } from './model/image.model';
import { User } from '../user/model/user.model';
import { PostService } from '../post/services/post.service';
import { UserService } from '../user/services/user.service';
import { Comment } from './model/comment.model';
import { Report } from 'src/app/report/model/report.model';
import { CommentService } from './services/comment.service';
import {JwtHelperService} from "@auth0/angular-jwt";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Reaction} from "./model/reaction.model";
import {ReactionService} from "./services/reaction.service";
import {ReportService} from "../report/services/report.service";
import {ReportReason} from "../report/model/reportReason";
import { map } from 'rxjs/operators'; // Import the map operator

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  reportReasons: ReportReason[] = Object.values(ReportReason);
  selectedReason: ReportReason | undefined;
  showReportDropdownForPost: boolean = false;
  showReportDropdownForComment: boolean = false;
  showReportDropdownForReply: boolean = false;
  showReportDropdownForUser: boolean = false;
  reportedCommentId: number | null = null;
  reportedReplyId: number | null = null;
  reportUserId: number | null = null;
  form: FormGroup;
  image: Image = new Image();
  post: Post = new Post();
  user: User = new User();
  images: Image[] = [];
  comments: Comment[] = [];
  replies: Comment[] = [];
  users: Map<number, User> = new Map();
  profileImageForm: FormGroup;
  reactions: Reaction[] = [];

  likes: number = 0;
  hearts: number = 0;
  dislikes: number = 0;

  commentsReactions: Map<number, Map<number, Reaction>> = new Map(); //Map<idKomentara, Map<idReakcije, Reakcija>> zbog brisanja reakcija na komentar

  defaultSortType: 'date' | 'likes' | 'hearts' | 'dislikes' = 'date';
  defaultSortDirection: 'asc' | 'desc' = 'asc';


  constructor(
    private postService: PostService,
    private userService: UserService,
    private reportService: ReportService,
    private commentService: CommentService,
    private router: Router,
    private reactionService: ReactionService,
    private fb: FormBuilder,

  ) {
    this.form = this.fb.group({
      image: [null],
    });
    this.profileImageForm = this.fb.group({
      profileImage: [null],
    });

  }
  openReportDropdown() {
    this.showReportDropdownForPost = true;
    this.showReportDropdownForUser = false;
  }
  reportDropdownOpened: boolean = false;


  ngOnInit(): void {
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

        this.reactionService.getReactionsForPost(this.post.id).subscribe(
          result => {
            this.reactions = result.body as Reaction[];

            this.reactions.forEach(reaction => {
              if (reaction.reactionType == 'LIKE')
                this.likes++;
              else if (reaction.reactionType == 'HEART')
                this.hearts++;
              else if (reaction.reactionType == 'DISLIKE')
                this.dislikes++;
            });
          },
          error => {
            window.alert('Error while retriving reactions for post');
            console.log(error);
          }
        );

        this.postService.getComments(this.post.id).subscribe(
          result => {
            let temp: Comment[] = result.body as unknown as Comment[];

            temp.forEach(comment => {
              if (comment.repliesToCommentId != null)
                this.replies.push(comment);
              else
                this.comments.push(comment);
            });

            temp.forEach(comment => {
              this.reactionService.getReactionsForComment(comment.id).subscribe(
                result => {
                  const reactions: Reaction[] = result.body as Reaction[];
                  const reactionsMap: Map<number, Reaction> = new Map();
                  reactions.forEach(reaction => {
                    reactionsMap.set(reaction.id, reaction);
                  });
                  this.commentsReactions.set(comment.id, reactionsMap);
                },
                error => {
                  window.alert('Error while retriving reactions to comment ' + comment.id);
                  console.log(error);
                }
              );
            });


            this.comments.forEach(comment => {
              this.userService.getOne(comment.belongsToUserId).subscribe(
                result => {
                  let user: User = result.body as User;
                  this.users.set(user.id, user);
                }
              )
            });

            this.replies.forEach(comment => {
              this.userService.getOne(comment.belongsToUserId).subscribe(
                result => {
                  let user: User = result.body as User;
                  this.users.set(user.id, user);
                }
              )
            });
          },
          error => {
            console.log('Error retrieving comments:', error);
            window.alert('Error while retrieving comments for post');
          }
        );

      }
    );
  }

  toggleSortDirection() {
    this.defaultSortDirection = this.defaultSortDirection === 'asc' ? 'desc' : 'asc';
  }

  sortComments(order: string) {
    this.postService.getSortedComments(this.post.id, order).subscribe(
      result => {
        if (result.status === 200) {
          let temp: Comment[] = result.body as unknown as Comment[];
          this.comments = temp;
        } else {
          window.alert('Error while sorting comments');
          console.log('Error status:', result.status);
        }
      },
      error => {
        window.alert('Error while sorting comments');
        console.log(error);
      }
    );
  }


  onSortOptionChange() {
    let order: string;

    switch (this.defaultSortType) {
      case 'date':
        order = this.defaultSortDirection === 'asc' ? 'timestamp_asc' : 'timestamp_desc';
        break;
      case 'likes':
        order = this.defaultSortDirection === 'asc' ? 'likes_asc' : 'likes_desc';
        break;
      case 'hearts':
        order = this.defaultSortDirection === 'asc' ? 'hearts_asc' : 'hearts_desc';
        break;
      case 'dislikes':
        order = this.defaultSortDirection === 'asc' ? 'dislikes_asc' : 'dislikes_desc';
        break;
      default:
        order = 'timestamp_asc'; // Default sorting order
        break;
    }

    this.sortComments(order);
  }




  canReply(id: number): boolean {
    const commentDiv = document.getElementById('comment' + id);
    const replyDiv = commentDiv?.querySelector('div.indented');
    if (replyDiv) {
      return false;
    } else {
      return true;
    }
  }

  reply(commentId: number) {
    let reply: Comment = new Comment();

    let text = prompt('Enter your reply:');
    if (text == null || text == "") {
      return;
    } else {
      reply.text = text;
    }

    reply.timestamp = new Date().toISOString().slice(0, 10);

    reply.belongsToPostId = this.post.id;
    reply.repliesToCommentId = commentId;

    let sub: string;
    const item = localStorage.getItem('user') || "";

    const jwt: JwtHelperService = new JwtHelperService();
    const decodedToken = jwt.decodeToken(item);
    sub = decodedToken.sub;

    this.userService.getOneByUsername(sub).subscribe(
      result => {
        const user: User = result.body as User;
        reply.belongsToUserId = user.id;

        this.commentService.add(reply).subscribe(
          result => {
            window.alert('Replied to comment');
            location.reload();
          },
          error => {
            window.alert('Error while replying to comment');
            console.log(error);
          }
        );
      }
    );
  }

  addComment() {
    let comment: Comment = new Comment();

    let text = prompt('Enter your comment:');
    if (text == null || text == "") {
      return;
    } else {
      comment.text = text;
    }

    comment.timestamp = new Date().toISOString().slice(0, 10);

    comment.belongsToPostId = this.post.id;

    let sub: string;
    const item = localStorage.getItem('user') || "";

    const jwt: JwtHelperService = new JwtHelperService();
    const decodedToken = jwt.decodeToken(item);
    sub = decodedToken.sub;

    this.userService.getOneByUsername(sub).subscribe(
      result => {
        const user: User = result.body as User;
        comment.belongsToUserId = user.id;

        this.commentService.add(comment).subscribe(
          result => {
            window.alert('Successfully added a comment');
            location.reload();
          },
          error => {
            window.alert('Error while adding a comment');
            console.log(error);
          }
        );
      }
    );
  }

  submitReport() {
    if (!this.selectedReason) {
      window.alert('Please select a valid report reason');
      return;
    }

    let report: Report = new Report();
    report.reason = this.selectedReason;
    report.timestamp = new Date().toISOString().slice(0, 10);

    report.onPostId = this.post.id;

    let sub: string;
    const item = localStorage.getItem('user') || "";

    const jwt: JwtHelperService = new JwtHelperService();
    const decodedToken = jwt.decodeToken(item);
    sub = decodedToken.sub;

    this.userService.getOneByUsername(sub).subscribe(
      result => {
        const user: User = result.body as User;
        report.byUserId = user.id;

        this.reportService.add(report).subscribe(
          result => {
            window.alert('Successfully added a report');
            location.reload();
          },
          error => {
            window.alert('Error while adding a report');
            console.log(error);
          }
        );
      }
    );
    this.showReportDropdownForPost = false;
    this.showReportDropdownForComment = false;
    this.showReportDropdownForReply = false;
  }
  submitReportComment(commentId: number) {
    if (!this.selectedReason) {
      window.alert('Please select a valid report reason');
      return;
    }

    let report: Report = new Report();
    report.reason = this.selectedReason;
    report.timestamp = new Date().toISOString().slice(0, 10);

    report.onCommentId = commentId;

    let sub: string;
    const item = localStorage.getItem('user') || "";

    const jwt: JwtHelperService = new JwtHelperService();
    const decodedToken = jwt.decodeToken(item);
    sub = decodedToken.sub;

    this.userService.getOneByUsername(sub).subscribe(
      result => {
        const user: User = result.body as User;
        report.byUserId = user.id;

        this.reportService.add(report).subscribe(
          result => {
            window.alert('Successfully reported the comment');
            location.reload();
          },
          error => {
            window.alert('Error while reporting the comment');
            console.log(error);
          }
        );
      }
    );
    this.showReportDropdownForPost = false;
    this.showReportDropdownForComment = false;
    this.showReportDropdownForReply = false;
    this.reportedCommentId = null;
  }

  submitUserReport() {
    if (!this.selectedReason) {
      window.alert('Please select a valid report reason');
      return;
    }

    if (!this.reportUserId) {
      window.alert('No user available to report');
      return;
    }

    let report: Report = new Report();
    report.reason = this.selectedReason;
    report.timestamp = new Date().toISOString().slice(0, 10);

    report.onUserId = this.reportUserId;

    let sub: string;
    const item = localStorage.getItem('user') || '';

    const jwt: JwtHelperService = new JwtHelperService();
    const decodedToken = jwt.decodeToken(item);
    sub = decodedToken.sub;

    this.userService.getOneByUsername(sub).subscribe(
      (result) => {
        const user: User = result.body as User;
        report.byUserId = user.id;

        this.reportService.add(report).subscribe(
          (result) => {
            window.alert('Successfully added a report');
            location.reload();
          },
          (error) => {
            window.alert('Error while adding a report');
            console.log(error);
          }
        );
      }
    );

    this.showReportDropdownForUser = false;
    this.showReportDropdownForPost = false;
    this.showReportDropdownForComment = false;
    this.showReportDropdownForReply = false;
  }

  openReportUserDropdown(reportUserId: number){
    this.reportUserId = reportUserId;
    this.showReportDropdownForUser = true;
    this.showReportDropdownForPost = false;
  }

  openReportCommentDropdown(commentId: number) {
    this.reportedCommentId = commentId;
  }

  submitReportReply(replyId: number) {
    if (!this.selectedReason) {
      window.alert('Please select a valid report reason');
      return;
    }

    let report: Report = new Report();
    report.reason = this.selectedReason;
    report.timestamp = new Date().toISOString().slice(0, 10);

    report.onCommentId = replyId;

    let sub: string;
    const item = localStorage.getItem('user') || "";

    const jwt: JwtHelperService = new JwtHelperService();
    const decodedToken = jwt.decodeToken(item);
    sub = decodedToken.sub;

    this.userService.getOneByUsername(sub).subscribe(
      result => {
        const user: User = result.body as User;
        report.byUserId = user.id;

        this.reportService.add(report).subscribe(
          result => {
            window.alert('Successfully reported the comment');
            location.reload();
          },
          error => {
            window.alert('Error while reporting the comment');
            console.log(error);
          }
        );
      }
    );
    this.showReportDropdownForPost = false;
    this.showReportDropdownForComment = false;
    this.showReportDropdownForReply = false;
    this.reportedReplyId = null;
  }

  openReportReplyDropdown(replyId: number) {
    this.reportedReplyId = replyId;
  }


  getReportReasonFromString(reason: string): ReportReason | undefined {
    switch (reason.trim()) {
      case ReportReason.BREAKS_RULES:
      case ReportReason.HARASSMENT:
      case ReportReason.HATE:
      case ReportReason.SHARING_PERSONAL_INFORMATION:
      case ReportReason.IMPERSONATION:
      case ReportReason.COPYRIGHT_VIOLATION:
      case ReportReason.TRADEMARK_VIOLATION:
      case ReportReason.SPAM:
      case ReportReason.SELF_HARM_OR_SUICIDE:
      case ReportReason.OTHER:
        return reason.trim() as ReportReason;
      default:
        return undefined;
    }
  }

  canDeleteComment(commentId: number): boolean {
    let sub: string;
    let role: string;
    const item = localStorage.getItem('user');
    let canDelete: boolean = false;

    if (!item) {
      this.router.navigate(['login']);
      return false;
    }

    const jwt: JwtHelperService = new JwtHelperService();
    const decodedToken = jwt.decodeToken(item);
    role = decodedToken.role.authority;
    sub = decodedToken.sub;

    this.comments.forEach(comment => {
      if (this.users.get(comment.belongsToUserId)?.username == sub && comment.id == commentId)
        canDelete = true;
    });

    if (role == 'ROLE_ADMIN')
      canDelete = true;

    return canDelete;
  }

  canDeleteReply(replyId: number): boolean {
    let sub: string;
    let role: string;
    const item = localStorage.getItem('user');
    let canDelete: boolean = false;

    if (!item) {
      this.router.navigate(['login']);
      return false;
    }

    const jwt: JwtHelperService = new JwtHelperService();
    const decodedToken = jwt.decodeToken(item);
    role = decodedToken.role.authority;
    sub = decodedToken.sub;

    this.replies.forEach(reply => {
      if (this.users.get(reply.belongsToUserId)?.username == sub && reply.id == replyId)
        canDelete = true;
    });

    if (role == 'ROLE_ADMIN')
      canDelete = true;

    return canDelete;
  }

  deleteComment(id: number) {
    this.commentService.delete(id).subscribe(
      result => {
        window.alert('Successfully deleted comment!');
        location.reload();
      },
      error => {
        window.alert('Error while deleting comment');
        console.log(error);
      }
    );
  }

  hasAuthority(): boolean {
    let role: string;
    let sub: string;
    const item = localStorage.getItem('user');

    if (!item) {
      this.router.navigate(['login']);
      role = "";
      return false;
    }

    const jwt: JwtHelperService = new JwtHelperService();
    const decodedToken = jwt.decodeToken(item);
    role = decodedToken.role.authority;
    sub = decodedToken.sub;

    if (role == 'ROLE_ADMIN' || sub == this.user.username)
      return true;
    return false;
  }

  deletePost() {
    this.postService.delete(this.post.id).subscribe(
      result => {
        window.alert('Successfully deleted the post!');
        this.router.navigate(['posts']).then(() => window.location.reload());;
      },
      error => {
        window.alert('Error while deleting post');
        console.log(error);
      }
    );
  }

  getReactionCount(commentId: number, reactionType: string): number {
    const reactions: Reaction[] = [];
    this.commentsReactions.get(commentId)?.forEach(reaction => {
      reactions.push(reaction);
    });
    let reactionCount: number = 0;

    reactions.forEach(reaction => {
      if (reaction.reactionType == reactionType)
        reactionCount++;
    });

    return reactionCount;
  }

  async reactedPost(postId: number, reactionType: string) {
    let reactorId: number = (await this.userService.extractUser() as User).id;
    let reaction = this.reactions.find(
      reaction => reaction.onPostId == postId && reaction.reactionType == reactionType && reaction.madeByUserId == reactorId);
    let previousReaction = this.reactions.find(reaction => reaction.onPostId == postId && reaction.madeByUserId == reactorId);

    if (reaction === undefined && previousReaction !== undefined) {
      this.reactionService.delete(previousReaction.id).subscribe(
        result => {
          if (reactionType == 'LIKE')
            this.likes--;
          else if (reactionType == 'HEART')
            this.hearts--;
          else if (reactionType == 'DISLIKE')
            this.dislikes--;
        },
        error => {
          window.alert('Error while removing reaction on post ' + postId);
          console.log(error);
        }
      );

      reaction = new Reaction();
      reaction.reactionType = reactionType;
      reaction.timestamp = new Date().toISOString().slice(0, 10);
      reaction.madeByUserId = reactorId;
      reaction.onPostId = postId;

      this.reactionService.add(reaction).subscribe(
        result => {
          if (reactionType == 'LIKE')
            this.likes++;
          else if (reactionType == 'HEART')
            this.hearts++;
          else if (reactionType == 'DISLIKE')
            this.dislikes++;
          location.reload();
        },
        error => {
          window.alert('Error while reacting on post ' + postId);
          console.log(error);
        }
      );

    } else if (reaction === undefined) {
      reaction = new Reaction();
      reaction.reactionType = reactionType;
      reaction.timestamp = new Date().toISOString().slice(0, 10);
      reaction.madeByUserId = reactorId;
      reaction.onPostId = postId;

      this.reactionService.add(reaction).subscribe(
        result => {
          if (reactionType == 'LIKE')
            this.likes++;
          else if (reactionType == 'HEART')
            this.hearts++;
          else if (reactionType == 'DISLIKE')
            this.dislikes++;
          location.reload();
        },
        error => {
          window.alert('Error while reacting on post ' + postId);
          console.log(error);
        }
      );
    } else {
      this.reactionService.delete(reaction.id).subscribe(
        result => {
          if (reactionType == 'LIKE')
            this.likes--;
          else if (reactionType == 'HEART')
            this.hearts--;
          else if (reactionType == 'DISLIKE')
            this.dislikes--;
          location.reload();
        },
        error => {
          window.alert('Error while removing reaction on post ' + postId);
          console.log(error);
        }
      );
    }
  }

  async reactedComment(commentId: number, reactionType: string) {
    let reactorId: number = (await this.userService.extractUser() as User).id;
    let reaction: Reaction = new Reaction();
    let previousReaction: Reaction = new Reaction();

    this.commentsReactions.get(commentId)?.forEach(temp => {
      if (temp.onCommentId == commentId && temp.reactionType == reactionType && temp.madeByUserId == reactorId)
        reaction = temp;
    });

    this.commentsReactions.get(commentId)?.forEach(temp => {
      if (temp.onCommentId == commentId && temp.madeByUserId == reactorId)
        previousReaction = temp;
    });

    if (reaction.id == null && previousReaction.id != null) {
      this.reactionService.delete(previousReaction.id).subscribe(
        result => {
          this.commentsReactions.get(commentId)?.delete(previousReaction.id);
        },
        error => {
          window.alert('Error while removing reaction on comment ' + commentId);
          console.log(error);
        }
      );

      reaction = new Reaction();
      reaction.reactionType = reactionType;
      reaction.timestamp = new Date().toISOString().slice(0, 10);
      reaction.madeByUserId = reactorId;
      reaction.onCommentId = commentId;

      this.reactionService.add(reaction).subscribe(
        result => {
          this.commentsReactions.get(commentId)?.set(reaction.id, reaction);
          location.reload();
        },
        error => {
          window.alert('Error while reacting on comment ' + commentId);
          console.log(error);
        }
      );

    } else if (reaction.id == null) {
      reaction = new Reaction();
      reaction.reactionType = reactionType;
      reaction.timestamp = new Date().toISOString().slice(0, 10);
      reaction.madeByUserId = reactorId;
      reaction.onCommentId = commentId;

      this.reactionService.add(reaction).subscribe(
        result => {
          this.commentsReactions.get(commentId)?.set(reaction.id, reaction);
          location.reload();
        },
        error => {
          window.alert('Error while reacting on comment ' + commentId);
          console.log(error);
        }
      );

    } else {
      this.reactionService.delete(reaction.id).subscribe(
        result => {
          this.commentsReactions.get(commentId)?.delete(previousReaction.id);
          location.reload();
        },
        error => {
          window.alert('Error while removing reaction on comment ' + commentId);
          console.log(error);
        }
      );
    }
  }
}

