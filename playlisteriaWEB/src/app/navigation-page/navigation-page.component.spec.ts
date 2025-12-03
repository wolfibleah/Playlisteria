import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavigationPageComponent } from './navigation-page.component';

describe('NavigationPageComponent', () => {
  let component: NavigationPageComponent;
  let fixture: ComponentFixture<NavigationPageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NavigationPageComponent],
    });
    fixture = TestBed.createComponent(NavigationPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
