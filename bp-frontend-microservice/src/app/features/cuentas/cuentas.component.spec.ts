import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CuentasComponent } from './cuentas.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CuentasService } from '../../core/services/cuentas.service';
import { FormBuilder } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { Account } from '../../core/models/account.module';
import { AcroFormCheckBox } from 'jspdf';


jest.mock('../../core/services/cuentas.service');
describe('CuentasComponent', () => {
  let component: CuentasComponent;
  let accountService: jest.Mocked<CuentasService>
  let formBuilder: FormBuilder;
  let fixture: ComponentFixture<CuentasComponent>;

  beforeEach(async () => {
    accountService = new CuentasService(null as any) as jest.Mocked<CuentasService>;
    accountService.getAccounts = jest.fn().mockReturnValue(of([]));
    accountService.createAccount = jest.fn().mockReturnValue(of({}));
    accountService.updateAccount = jest.fn().mockReturnValue(of({}));
    accountService.deleteAccount = jest.fn().mockReturnValue(of({}));
    formBuilder = new FormBuilder();

    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, CuentasComponent], // ✅ Agregar módulo para evitar el error de HttpClient
      providers: [
        { provide: CuentasService, useValue: accountService},
        { provide: FormBuilder, useValue: formBuilder}
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CuentasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debería cargar las cuentas correctamente', () => {
    const mockAccounts:Account[] = [{ id: 1, accountNumber: '12345', accountType: 'AHORROS', balance: 500, state: "ACTIVA", clientId: 1 }];
    accountService.getAccounts.mockReturnValue(of(mockAccounts));
    component.cargarAccounts();
    expect(component.accounts.length).toBe(1);
    expect(component.accounts[0].accountNumber).toBe('12345');
  });
  
  it('debería manejar error al cargar cuentas', () => {
    accountService.getAccounts.mockReturnValue(throwError(() => new Error('Error de carga')));
    component.cargarAccounts();
    expect(component.mensajeError).toBe('Error de carga');
  });

  it('debería validar el formulario antes de guardar', () => {
    component.guardarAccount();
    expect(component.accountForm.invalid).toBeTruthy();
  });

  it('debería filtrar clcuentas correctamente', () => {
    component.accounts = [
      { id: 1, accountNumber: '12345', accountType: 'AHORROS', balance: 500, state: "ACTIVA", clientId: 1 },
      { id: 2, accountNumber: '54321', accountType: 'CORRIENTE', balance: 500, state: "INACTIVA", clientId: 2 }
    ];
    component.filtrarAccounts('1');
    expect(component.accountsFiltradas.length).toBe(1);
    expect(component.accountsFiltradas[0].accountNumber).toBe('12345');
  });

  it('debería llamar al servicio para eliminar una cuenta', () => {
    global.confirm = jest.fn(() => true);
    accountService.deleteAccount.mockReturnValue(of(undefined));
    component.deleteAccount(1);
    expect(accountService.deleteAccount).toHaveBeenCalledWith(1);
  });
});
