import { TestBed, inject } from '@angular/core/testing';

import { ShipAnimationService } from './ship-animation.service';

describe('ShipAnimationService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ShipAnimationService]
    });
  });

  it('should be created', inject([ShipAnimationService], (service: ShipAnimationService) => {
    expect(service).toBeTruthy();
  }));
});
