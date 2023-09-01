import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupAdminsComponent } from './group-admins.component';

describe('GroupAdminsComponent', () => {
  let component: GroupAdminsComponent;
  let fixture: ComponentFixture<GroupAdminsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GroupAdminsComponent ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GroupAdminsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
