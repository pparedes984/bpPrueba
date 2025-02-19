import { TestBed } from '@angular/core/testing';

import { CuentasService } from './cuentas.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('CuentasService', () => {
  let service: CuentasService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports : [HttpClientTestingModule]
    });
    service = TestBed.inject(CuentasService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
