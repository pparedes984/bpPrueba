import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovimientosComponent } from './movimientos.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MovimientosService } from '../../core/services/movimientos.service';
import { FormBuilder } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { Transaction } from '../../core/models/transaction.module';

jest.mock('../../core/services/movimientos.service');
describe('MovimientosComponent', () => {
  let component: MovimientosComponent;
  let fixture: ComponentFixture<MovimientosComponent>;
  let transactionService: jest.Mocked<MovimientosService>
  let formBuilder: FormBuilder;

  beforeEach(async () => {
    transactionService = new MovimientosService(null as any) as jest.Mocked<MovimientosService>;
    transactionService.getTransaction = jest.fn().mockReturnValue(of([]));
    transactionService.createTransaction = jest.fn().mockReturnValue(of({}));
    transactionService.updateTransaction = jest.fn().mockReturnValue(of({}));
    transactionService.deleteTransaction = jest.fn().mockReturnValue(of({}));
    formBuilder = new FormBuilder();

    await TestBed.configureTestingModule({
      imports: [MovimientosComponent, HttpClientTestingModule],
      providers : [ 
        { provide: MovimientosService, useValue: transactionService},
        { provide: FormBuilder, useValue: formBuilder}
       ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovimientosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('debería cargar las transacciones correctamente', () => {
      const mockTransactions:Transaction[] = [{ id: 1, date: '2025-02-17T16:00:35.924477', transactionType: 'DEBITO', value: 100, balance: 500, accountId: 1 }];
      transactionService.getTransaction.mockReturnValue(of(mockTransactions));
      component.cargarTransactions();
      expect(component.transactions.length).toBe(1);
      expect(component.transactions[0].id).toBe(1);
    });
    
  it('debería manejar error al cargar transacciones', () => {
    transactionService.getTransaction.mockReturnValue(throwError(() => new Error('Error de carga')));
    component.cargarTransactions();
    expect(component.mensajeError).toBe('Error de carga');
  });

  it('debería validar el formulario antes de guardar', () => {
    component.guardarTransaction();
    expect(component.transactionForm.invalid).toBeTruthy();
  });

  it('debería filtrar transacciones correctamente', () => {
    component.transactions = [
      { id: 1, date: '2025-02-17T16:00:35.924477', transactionType: 'DEBITO', value: 100, balance: 500, accountId: 1 },
      { id: 2, date: '2025-02-17T16:00:35.924477', transactionType: 'CREDITO', value: 800, balance: 1500, accountId: 2 }
    ];
    component.filtrarTransactions('1');
    expect(component.transactionsFiltradas.length).toBe(1);
    expect(component.transactionsFiltradas[0].id).toBe(1);
  });

  it('debería llamar al servicio para eliminar una transaccion', () => {
    global.confirm = jest.fn(() => true);
    transactionService.deleteTransaction.mockReturnValue(of(undefined));
    component.deleteTransaction(1);
    expect(transactionService.deleteTransaction).toHaveBeenCalledWith(1);
  });
});
