import { Component, OnInit } from '@angular/core';
import { Report } from 'src/app/report/model/report.model';
import {GroupService} from "src/app/group/services/group.service";
import {Router} from "@angular/router";
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-group-reports',
  templateUrl: './group-reports.component.html',
  styleUrls: ['./group-reports.component.css']
})
export class GroupReportsComponent implements OnInit {

  reports: Report[] = [];

  constructor(
    private groupService: GroupService,
    private route: ActivatedRoute, // Inject ActivatedRoute
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const id = +params['id']; // + symbol to convert string to number
      if (!isNaN(id)) { // Check if id is a valid number
        this.groupService.getReportsForGroup(id).subscribe(
          result => {
            this.reports = result.body as Report[];
          }
        );
      } else {
        console.error('Invalid Group ID:', params['id']);
      }
    });
  }

}
