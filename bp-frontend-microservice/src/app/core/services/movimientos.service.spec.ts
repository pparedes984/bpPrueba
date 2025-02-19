import { TestBed } from '@angular/core/testing';

import { MovimientosService } from './movimientos.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('MovimientosService', () => {
  let service: MovimientosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports : [HttpClientTestingModule]
    });
    service = TestBed.inject(MovimientosService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
