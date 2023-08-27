import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Report } from "src/app/report/model/report.model";
import { User } from 'src/app/user/model/user.model';
import { UserService } from 'src/app/user/services/user.service';
import { ReportService } from 'src/app/report/services/report.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Location } from '@angular/common';


@Component({
  selector: 'app-edit-report-group',
  templateUrl: './edit-report-group.component.html',
  styleUrls: ['./edit-report-group.component.css']
})
export class EditReportGroupComponent implements OnInit {

  form: FormGroup;
  editing: boolean = this.router.url.includes('edit');

  report: Report = new Report();
  user: User = new User();

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private reportService: ReportService,
    private location: Location,
    private router: Router
  ) {
    this.form = this.fb.group({
      accepted: [null, Validators.required]
    });

    if (this.editing) {
      const id: number = Number.parseInt(this.router.url.split('/')[4]);
      this.reportService.getOne(id).subscribe(
        result => {
          this.report = result.body as Report;
          this.form.patchValue(this.report);
        },
        error => {
          window.alert('An error occurred retriving report!');
          console.log(error);
        }
      );
    }
  }

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
  }

  submit(): void {
    this.report.accepted = this.form.value.accepted;

    this.reportService.edit(this.report).subscribe(
      result => {
        window.alert('Successfully edited the report');
        this.location.back();
      },
      error => {
        window.alert('An error occurred editing the report');
        console.log(error);
      }
    );

  }
}
