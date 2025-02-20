import { TestBed } from '@angular/core/testing';

import { MovimientosService } from './movimientos.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { environment } from '../../../environments/environment';
import { Transaction } from '../models/transaction.module';

describe('MovimientosService', () => {
  let service: MovimientosService;
   let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [MovimientosService]
    });
    service = TestBed.inject(MovimientosService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('debería obtener la lista de cuentas', () => {
      const mockTransactions: Transaction[] = [{ id: 1, date: "2025-02-17T16:00:35.924477", transactionType: "DEBITO", value: 500, balance: 500, accountId: 1  }];
      
      service.getTransaction().subscribe(transactions => {
        expect(transactions).toEqual(mockTransactions);
      });
  
      const req = httpMock.expectOne(`${environment.API_URL}movimientos`);
      expect(req.request.method).toBe('GET');
      req.flush(mockTransactions);
    });

  it('debería crear una cuenta', () => {
      const newTransaction: Transaction = { id: 1, date: "2025-02-17T16:00:35.924477", transactionType: "DEBITO", value: 500, balance: 500, accountId: 1  };
  
      service.createTransaction(newTransaction).subscribe(response => {
        expect(response).toEqual(newTransaction);
      });
  
      const req = httpMock.expectOne(`${environment.API_URL}movimientos/${newTransaction.accountId}`);
      expect(req.request.method).toBe('POST');
      req.flush(newTransaction);
    });
  
    it('debería actualizar una cuenta', () => {
      const updatedTransaction: Transaction = { id: 1, date: "2025-02-17T16:00:35.924477", transactionType: "DEBITO", value: 500, balance: 500, accountId: 1  };
  
      service.updateTransaction(1, updatedTransaction).subscribe(response => {
        expect(response).toEqual(updatedTransaction);
      });
  
      const req = httpMock.expectOne(`${environment.API_URL}movimientos/${updatedTransaction.id}`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.method).toBe('PUT');
      req.flush(updatedTransaction);
    });
  
    it('debería eliminar una cuenta', () => {
      const transactionId = 1;
      service.deleteTransaction(transactionId).subscribe(response => {
        expect(response).toBeNull();
      });
  
      const req = httpMock.expectOne(`${environment.API_URL}movimientos/${transactionId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });
});
