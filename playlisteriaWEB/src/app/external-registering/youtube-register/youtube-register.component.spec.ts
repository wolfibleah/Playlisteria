import { ComponentFixture, TestBed } from '@angular/core/testing';

import { YoutubeRegisterComponent } from './youtube-register.component';

describe('YoutubeRegisterComponent', () => {
  let component: YoutubeRegisterComponent;
  let fixture: ComponentFixture<YoutubeRegisterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [YoutubeRegisterComponent]
    });
    fixture = TestBed.createComponent(YoutubeRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
