import { Component, OnInit } from '@angular/core';
import { GroupRequest } from '../model/groupRequest.model';
import { User } from 'src/app/user/model/user.model';
import { UserService } from 'src/app/user/services/user.service';
import { GroupService } from '../services/group.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-group-requests',
  templateUrl: './group-requests.component.html',
  styleUrls: ['./group-requests.component.css']
})
export class GroupRequestsComponent implements OnInit {

  recievedRequests: GroupRequest[] = [];
  requestUsers: Map<number, User> = new Map(); //id korisnika i korisnik za template

  constructor(
    private groupService: GroupService,
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id: number = Number.parseInt(this.router.url.split('/')[2]);
    this.groupService.getGroupRequests(id).subscribe(
      result => {
        this.recievedRequests = result.body as GroupRequest[];

        this.recievedRequests.forEach(request => {
          this.userService.getOne(request.createdByUserId).subscribe(
            result => {
              const user: User = result.body as User;
              this.requestUsers.set(user.id, user);
            }
          );
        });
      }
    );
  }

  respondToRequest(id: number, response: string): void {
    let respondedRequest: GroupRequest = new GroupRequest();
    this.recievedRequests.forEach(request => {
      if (request.id == id)
        respondedRequest = request;
    });

    if (response == 'accept')
      respondedRequest.approved = true;
    else if (response == 'decline')
      respondedRequest.approved = false;

    respondedRequest.at =  new Date().toISOString().slice(0, -1);

    this.groupService.updateGroupRequest(respondedRequest).subscribe(
      result => {
        window.alert('Successfully responded to group request');
      },
      error => {
        window.alert('Error while responding to group request');
        console.log(error);
      }
    );
    if (response == 'decline')
      this.groupService.deleteGroupRequest(respondedRequest.id).subscribe();
  }

  deleteRequest(id: number): void {
    this.userService.deleteFriendRequest(id).subscribe(
      result => {
        window.alert('Successfully deleted group request');
        location.reload();
      },
      error => {
        window.alert('Error while deleting group request');
        console.log(error);
      }
    );
  }
}
