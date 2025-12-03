import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MusicToastComponent } from './music-toast.component';

describe('MusicToastComponent', () => {
  let component: MusicToastComponent;
  let fixture: ComponentFixture<MusicToastComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MusicToastComponent]
    });
    fixture = TestBed.createComponent(MusicToastComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
