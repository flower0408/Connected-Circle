import { Component, OnInit } from '@angular/core';
import { Banned } from 'src/app/banned/model/banned.model';
import { BannedService } from 'src/app/banned/services/banned.service';
import {User} from "src/app/user/model/user.model";
import {UserService} from "src/app/user/services/user.service";
import {Location} from "@angular/common";
import {ActivatedRoute, Router} from "@angular/router";
import {JwtHelperService} from "@auth0/angular-jwt";


@Component({
  selector: 'app-banned-list-group',
  templateUrl: './banned-list-group.component.html',
  styleUrls: ['./banned-list-group.component.css']
})
export class BannedListGroupComponent implements OnInit{

  bans: Banned[] = [];

  banned: Banned = new Banned();
  user: User = new User();

  constructor(
    private bannedService: BannedService,
    private userService: UserService,
    private location: Location,
    private router: Router,
    private route: ActivatedRoute // Inject ActivatedRoute
  ) { }

  ngOnInit(): void {

    const jwt: JwtHelperService = new JwtHelperService();
    const userToken: string = localStorage.getItem('user') || '';
    const decoded = jwt.decodeToken(userToken);

    this.userService.getOneByUsername(decoded.sub).subscribe(
      result => {
        this.user = result.body as User;
      },
      error => {
        window.alert('Couln\'t find logged in user');
        console.log(error);
      }
    );

    this.route.params.subscribe(params => {
      const groupId = +params['groupId'];
      if (!isNaN(groupId)) {
        this.bannedService.getAllGroup(groupId).subscribe(
          result => {
            this.bans = result.body as Banned[];
          }
        );
      } else {
        console.error('Invalid Group ID:', params['groupId']);
      }
    });
  }

  submitBannedGroup(banned: Banned): void {
    this.bannedService.unblockMember(banned.id).subscribe(
      result => {
        window.alert('Successfully edited the banned');
        this.location.back();
      },
      error => {
        window.alert('An error occurred editing the banned');
        console.log(error);
      }
    );
  }

}
