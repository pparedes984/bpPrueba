import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientesComponent } from './clientes.component';
import { ClientesService } from '../../core/services/clientes.service';
import { ReactiveFormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Client } from '../../core/models/client.module';

jest.mock('../../core/services/clientes.service');

describe('ClientesComponent', () => {
  let component: ClientesComponent;
  let fixture: ComponentFixture<ClientesComponent>;
  let clientesService: jest.Mocked<ClientesService>;

  beforeEach(async () => {
    clientesService = new ClientesService(null as any) as jest.Mocked<ClientesService>;
    clientesService.getClients = jest.fn();
    clientesService.createClient = jest.fn();
    clientesService.updateClient = jest.fn();
    clientesService.deleteClient = jest.fn();

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, HttpClientTestingModule, ClientesComponent ],
      providers: [{ provide: ClientesService, useValue: clientesService }]
    }).compileComponents();

    fixture = TestBed.createComponent(ClientesComponent);
    component = fixture.componentInstance;
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debería cargar los clientes correctamente', () => {
    const mockClients:Client[] = [{ id: 1, name: 'Juan', dni: '12345678', gender: 'MASCULINO', age: 20, address: 'La luz', state: 'ACTIVO', telephone: '123456', password: 'test' }];
    clientesService.getClients.mockReturnValue(of(mockClients));
    component.cargarClientes();
    expect(component.clients.length).toBe(1);
    expect(component.clients[0].name).toBe('Juan');
  });

  it('debería manejar error al cargar clientes', () => {
    clientesService.getClients.mockReturnValue(throwError(() => new Error('Error de carga')));
    component.cargarClientes();
    expect(component.mensajeError).toBe('Error de carga');
  });

  it('debería filtrar clientes correctamente', () => {
    component.clients = [
      { id: 1, name: 'Juan', dni: '12345678', gender: 'MASCULINO', age: 20, address: 'La luz', state: 'ACTIVO', telephone: '123456', password: 'test' },
      { id: 2, name: 'Ana', dni: '87654321', gender: 'FEMENINO', age: 30, address: 'Conocoto', state: 'ACTIVO', telephone: '123456', password: 'test' }
    ];
    component.filtrarClientes('1');
    expect(component.clientesFiltrados.length).toBe(1);
    expect(component.clientesFiltrados[0].name).toBe('Juan');
  });

  it('debería llamar al servicio para eliminar un cliente', () => {
    global.confirm = jest.fn(() => true);
    clientesService.deleteClient.mockReturnValue(of(undefined));
    component.deleteClient(1);
    expect(clientesService.deleteClient).toHaveBeenCalledWith(1);
  });
});
