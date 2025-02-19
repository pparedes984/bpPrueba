import { TestBed } from '@angular/core/testing';

import { ReportesService } from './reportes.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ReportesService', () => {
  let service: ReportesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports : [HttpClientTestingModule]
    });
    service = TestBed.inject(ReportesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
