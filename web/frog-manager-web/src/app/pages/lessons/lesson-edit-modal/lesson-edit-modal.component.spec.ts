import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import {LessonEditModalComponent} from './lesson-edit-modal.component';

describe('LessonEditModalComponent', () => {
  let component: LessonEditModalComponent;
  let fixture: ComponentFixture<LessonEditModalComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [LessonEditModalComponent]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LessonEditModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
