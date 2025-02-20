import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientesComponent } from './clientes.component';
import { ClientesService } from '../../core/services/clientes.service';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Client } from '../../core/models/client.module';

jest.mock('../../core/services/clientes.service');

describe('ClientesComponent', () => {
  let component: ClientesComponent;
  let clientesService: jest.Mocked<ClientesService>;
  let formBuilder: FormBuilder;
  let fixture: ComponentFixture<ClientesComponent>;

  beforeEach(async () => {
    clientesService = new ClientesService(null as any) as jest.Mocked<ClientesService>;
    clientesService.getClients = jest.fn().mockReturnValue(of([]));
    clientesService.createClient = jest.fn().mockReturnValue(of({}));
    clientesService.updateClient = jest.fn().mockReturnValue(of({}));
    clientesService.deleteClient = jest.fn().mockReturnValue(of({}));
    formBuilder = new FormBuilder();

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, HttpClientTestingModule, ClientesComponent ],
      
      providers: [
        { provide: ClientesService, useValue: clientesService },
        { provide: FormBuilder, useValue: formBuilder }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ClientesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
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

  it('debería validar el formulario antes de guardar', () => {
    component.guardarCliente();
    expect(component.clienteForm.invalid).toBeTruthy();
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
