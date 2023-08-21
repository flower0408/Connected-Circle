import { Component, OnInit } from '@angular/core';
import { FriendRequest } from '../model/friendRequest.model';
import { User } from '../model/user.model';
import { JwtHelperService } from '@auth0/angular-jwt';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-friend-requests',
  templateUrl: './friend-requests.component.html',
  styleUrls: ['./friend-requests.component.css']
})
export class FriendRequestsComponent implements OnInit {

  user: User = new User();
  friendRequests: FriendRequest[] = [];
  sentRequests: FriendRequest[] = [];
  recievedRequests: FriendRequest[] = [];

  requestUsers: Map<number, User> = new Map(); //id korisnika i korisnik za template

  constructor(
    private userService: UserService
  ) { }

  ngOnInit(): void {
    const item: string = localStorage.getItem('user') || '';
    const jwt: JwtHelperService = new JwtHelperService();
    const sub: string = jwt.decodeToken(item).sub;

    this.userService.getOneByUsername(sub).subscribe(
      result => {
        this.user = result.body as User;

        this.userService.getFriendRequests().subscribe(
          result => {
            this.friendRequests = result.body as FriendRequest[];

            this.friendRequests.forEach(request => {
              if (request.fromUserId == this.user.id)
                this.sentRequests.push(request);
              else if (request.toUserId == this.user.id)
                this.recievedRequests.push(request);
            });

            this.sentRequests.forEach(request => {
                this.userService.getOne(request.toUserId).subscribe(
                  result => {
                    const user: User = result.body as User;
                    this.requestUsers.set(user.id, user);
                  }
                );
            });

            this.recievedRequests.forEach(request => {
              this.userService.getOne(request.fromUserId).subscribe(
                result => {
                  const user: User = result.body as User;
                  this.requestUsers.set(user.id, user);
                }
              );
            });
          }
        );
      }
    );
  }

  respondToRequest(id: number, response: string): void {
    let respondedRequest: FriendRequest = new FriendRequest();
    this.friendRequests.forEach(request => {
      if (request.id == id)
        respondedRequest = request;
    });

    if (response == 'accept')
      respondedRequest.approved = true;
    else if (response == 'decline')
      respondedRequest.approved = false;

    respondedRequest.at =  new Date().toISOString().slice(0, -1);

    this.userService.updateFriendRequest(respondedRequest).subscribe(
      result => {
        window.alert('Successfully responded to friend request');
      },
      error => {
        window.alert('Error while responding to friend request');
        console.log(error);
      }
    );
  }

  deleteRequest(id: number): void {
    this.userService.deleteFriendRequest(id).subscribe(
      result => {
        window.alert('Successfully deleted friend request');
        location.reload();
      },
      error => {
        window.alert('Error while deleting friend request');
        console.log(error);
      }
    );
  }
}
