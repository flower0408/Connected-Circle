import { Component, OnInit } from '@angular/core';
import { Report } from '../model/report.model';
import { ReportService } from '../services/report.service';

@Component({
  selector: 'app-report-list',
  templateUrl: './report-list.component.html',
  styleUrls: ['./report-list.component.css']
})
export class ReportListComponent implements OnInit{

  reports: Report[] = [];

  constructor(
    private reportService: ReportService,
  ) { }

  ngOnInit(): void {
    this.reportService.getAllReports().subscribe(
      result => {
        this.reports = result.body as Report[];

      }
    );
  }
}
