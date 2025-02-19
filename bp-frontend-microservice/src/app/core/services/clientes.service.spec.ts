import { TestBed } from '@angular/core/testing';

import { ClientesService } from './clientes.service';
import { Client } from '../models/client.module';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { environment } from '../../../environments/environment';

describe('ClientesService', () => {
  let service: ClientesService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ClientesService]
    });
    service = TestBed.inject(ClientesService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('debería obtener la lista de clientes', () => {
    const mockClients: Client[] = [{ id: 1, name: 'Juan', dni: '12345678', telephone: '5551234', address: 'Calle 123', gender: 'M', age: 30, state: 'ACTIVO', password: '1234' }];
    
    service.getClients().subscribe(clients => {
      expect(clients).toEqual(mockClients);
    });

    const req = httpMock.expectOne(`${environment.API_URL}clientes`);
    expect(req.request.method).toBe('GET');
    req.flush(mockClients);
  });

  it('debería crear un cliente', () => {
    const newClient: Client = { id: 2, name: 'Ana', dni: '87654321', telephone: '5555678', address: 'Avenida 456', gender: 'F', age: 25, state: 'ACTIVO', password: 'abcd' };

    service.createClient(newClient).subscribe(response => {
      expect(response).toEqual(newClient);
    });

    const req = httpMock.expectOne(`${environment.API_URL}clientes`);
    expect(req.request.method).toBe('POST');
    req.flush(newClient);
  });

  it('debería actualizar un cliente', () => {
    const updatedClient: Client = { id: 1, name: 'Juan Pérez', dni: '12345678', telephone: '5551234', address: 'Calle 123', gender: 'M', age: 30, state: 'INACTIVO', password: '1234' };

    service.updateClient(1, updatedClient).subscribe(response => {
      expect(response).toEqual(updatedClient);
    });

    const req = httpMock.expectOne(`${environment.API_URL}clientes/${updatedClient.id}`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.method).toBe('PUT');
    req.flush(updatedClient);
  });

  it('debería eliminar un cliente', () => {
    const clientId = 1;
    service.deleteClient(clientId).subscribe(response => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne(`${environment.API_URL}clientes/${clientId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
