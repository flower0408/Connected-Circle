import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BannedListGroupComponent } from './banned-list-group.component';

describe('BannedListGroupComponent', () => {
  let component: BannedListGroupComponent;
  let fixture: ComponentFixture<BannedListGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BannedListGroupComponent ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BannedListGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
