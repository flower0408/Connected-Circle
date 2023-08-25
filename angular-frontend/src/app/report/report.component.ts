import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReportService } from 'src/app/report/services/report.service';
import { Report } from 'src/app/report/model/report.model';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})

export class ReportComponent implements OnInit {

  report: Report | null = null;

  constructor(
    private reportService: ReportService,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id: number = Number.parseInt(this.router.url.split('/')[2]);

    this.reportService.getOne(id).subscribe(
      result => {
        this.report = result.body;
      }
    );
  }
}

