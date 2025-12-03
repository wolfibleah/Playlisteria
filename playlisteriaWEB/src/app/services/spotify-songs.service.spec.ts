import { TestBed } from '@angular/core/testing';

import { SpotifySongsService } from './spotify-songs.service';

describe('SpotifySongsService', () => {
  let service: SpotifySongsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpotifySongsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
