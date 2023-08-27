import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupReportsComponent } from './group-reports.component';

describe('GroupReportsComponent', () => {
  let component: GroupReportsComponent;
  let fixture: ComponentFixture<GroupReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GroupReportsComponent ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GroupReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
