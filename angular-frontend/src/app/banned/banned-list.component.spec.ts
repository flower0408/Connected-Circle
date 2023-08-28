import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BannedListComponent } from './banned-list.component';

describe('BannedListComponent', () => {
  let component: BannedListComponent;
  let fixture: ComponentFixture<BannedListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BannedListComponent ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BannedListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
