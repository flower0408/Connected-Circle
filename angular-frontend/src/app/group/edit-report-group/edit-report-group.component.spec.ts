import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditReportGroupComponent } from './edit-report-group.component';

describe('EditReportGroupComponent', () => {
  let component: EditReportGroupComponent;
  let fixture: ComponentFixture<EditReportGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditReportGroupComponent ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(EditReportGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
