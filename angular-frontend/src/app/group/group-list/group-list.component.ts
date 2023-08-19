import { Component } from '@angular/core';
import { Group } from '../model/group.model';

@Component({
  selector: 'app-group-list',
  templateUrl: './group-list.component.html',
  styleUrls: ['./group-list.component.css']
})
export class GroupListComponent {
  groups: Group[] = [];

  constructor() {

  }

  ngOnInit(): void {

  }
}
