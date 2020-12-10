import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {SearchSettingsComponent} from './search-settings.component';

describe('SearchSettingsComponent', () => {
  let component: SearchSettingsComponent;
  let fixture: ComponentFixture<SearchSettingsComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [SearchSettingsComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
