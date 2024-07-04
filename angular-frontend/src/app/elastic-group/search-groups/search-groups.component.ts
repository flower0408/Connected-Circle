import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { GroupService } from 'src/app/group/services/group.service';
import { GroupElastic } from 'src/app/elastic-group/model/elasticGroup.model';

@Component({
  selector: 'app-search-groups',
  templateUrl: './search-groups.component.html',
  styleUrls: ['./search-groups.component.css']
})
export class SearchGroupsComponent implements OnInit {
    searchForm: FormGroup;
    groups: GroupElastic[] = [];
  
    constructor(private fb: FormBuilder, private groupService: GroupService) {
      this.searchForm = this.fb.group({
        name: [''],
        description: [''],
        pdfContent: [''],
        greaterThan: [null],
        lessThan: [null],
        operation: ['OR'],
        usePhraseQuery: [false],
        useFuzzyQuery: [false]
      });
    }
  
    ngOnInit(): void {}
  
    searchByName() {
      const name = this.searchForm.get('name')?.value;
      const usePhraseQuery = this.searchForm.get('usePhraseQuery')?.value;
      const useFuzzyQuery = this.searchForm.get('useFuzzyQuery')?.value;
      if (name) {
        this.groupService.getElasticGroupsByName(name, usePhraseQuery, useFuzzyQuery).subscribe(groups => this.groups = groups);
      }
    }
  
    searchByDescription() {
      const description = this.searchForm.get('description')?.value;
      const usePhraseQuery = this.searchForm.get('usePhraseQuery')?.value;
      const useFuzzyQuery = this.searchForm.get('useFuzzyQuery')?.value;
      if (description) {
        this.groupService.getElasticGroupsByDescription(description, usePhraseQuery, useFuzzyQuery).subscribe(groups => this.groups = groups);
      }
    }
  
    searchByPDFContent() {
      const content = this.searchForm.get('pdfContent')?.value;
      const usePhraseQuery = this.searchForm.get('usePhraseQuery')?.value;
      const useFuzzyQuery = this.searchForm.get('useFuzzyQuery')?.value;
      if (content) {
        this.groupService.getElasticGroupsByPDFContent(content, usePhraseQuery, useFuzzyQuery).subscribe(groups => this.groups = groups);
      }
    }
  
    searchByPostsRange() {
      const greaterThan = this.searchForm.get('greaterThan')?.value;
      const lessThan = this.searchForm.get('lessThan')?.value;
      const data = { greaterThan, lessThan };
      this.groupService.getElasticGroupsByPostsRange(data).subscribe(groups => this.groups = groups);
    }
  
    searchBooleanQuery() {
      const name = this.searchForm.get('name')?.value;
      const description = this.searchForm.get('description')?.value;
      const pdfContent = this.searchForm.get('pdfContent')?.value;
      const operation = this.searchForm.get('operation')?.value;
      const usePhraseQuery = this.searchForm.get('usePhraseQuery')?.value;
      const useFuzzyQuery = this.searchForm.get('useFuzzyQuery')?.value;
      this.groupService.searchElasticGroups(name, description, pdfContent, operation, usePhraseQuery, useFuzzyQuery)
        .subscribe(groups => this.groups = groups);
    }
  }