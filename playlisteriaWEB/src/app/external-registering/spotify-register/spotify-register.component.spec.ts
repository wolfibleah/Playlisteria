import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpotifyRegisterComponent } from './spotify-register.component';

describe('SpotifyRegisterComponent', () => {
  let component: SpotifyRegisterComponent;
  let fixture: ComponentFixture<SpotifyRegisterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SpotifyRegisterComponent]
    });
    fixture = TestBed.createComponent(SpotifyRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
