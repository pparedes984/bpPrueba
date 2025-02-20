import { TestBed } from '@angular/core/testing';

import { CuentasService } from './cuentas.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { environment } from '../../../environments/environment';
import { Account } from '../models/account.module';

describe('CuentasService', () => {
  let service: CuentasService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CuentasService]
    });
    service = TestBed.inject(CuentasService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('debería obtener la lista de cuentas', () => {
      const mockAccounts: Account[] = [{ id: 1, accountNumber: "123", accountType: "AHORROS", balance: 500, state: "ACTIVA", clientId: 1  }];
      
      service.getAccounts().subscribe(accounts => {
        expect(accounts).toEqual(mockAccounts);
      });
  
      const req = httpMock.expectOne(`${environment.API_URL}cuentas`);
      expect(req.request.method).toBe('GET');
      req.flush(mockAccounts);
    });

  it('debería crear una cuenta', () => {
      const newAccount: Account = { id: 1, accountNumber: "123", accountType: "AHORROS", balance: 500, state: "ACTIVA", clientId: 1  };
  
      service.createAccount(newAccount).subscribe(response => {
        expect(response).toEqual(newAccount);
      });
  
      const req = httpMock.expectOne(`${environment.API_URL}cuentas`);
      expect(req.request.method).toBe('POST');
      req.flush(newAccount);
    });
  
    it('debería actualizar una cuenta', () => {
      const updatedAccount: Account = { id: 1, accountNumber: "123", accountType: "AHORROS", balance: 500, state: "ACTIVA", clientId: 1  };
  
      service.updateAccount(1, updatedAccount).subscribe(response => {
        expect(response).toEqual(updatedAccount);
      });
  
      const req = httpMock.expectOne(`${environment.API_URL}cuentas/${updatedAccount.id}`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.method).toBe('PUT');
      req.flush(updatedAccount);
    });
  
    it('debería eliminar una cuenta', () => {
      const accountId = 1;
      service.deleteAccount(accountId).subscribe(response => {
        expect(response).toBeNull();
      });
  
      const req = httpMock.expectOne(`${environment.API_URL}cuentas/${accountId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });
});
