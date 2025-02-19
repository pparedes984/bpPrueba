import { Component, OnInit } from '@angular/core';
import { Transaction } from '../../core/models/transaction.module';
import { CommonModule } from '@angular/common';
import { MovimientosService } from '../../core/services/movimientos.service';
import { transition } from '@angular/animations';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { debounceTime } from 'rxjs';
import { CuentasService } from '../../core/services/cuentas.service';
import { Account } from '../../core/models/account.module';

@Component({
  selector: 'app-movimientos',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  standalone: true,
  templateUrl: './movimientos.component.html',
  styleUrl: './movimientos.component.scss'
})
export class MovimientosComponent implements OnInit{
  transactions: Transaction[] = [];
  transactionsFiltradas: Transaction[] = [];
  formularioVisible = false;
  editando = false;
  filtroId = '';
  transactionForm: FormGroup;
  mensajeError: string | null = null;
  accounts: Account[] = [];

  constructor(
    private transactionService: MovimientosService, 
    private fb: FormBuilder,
    private accountService: CuentasService) {
    this.transactionForm = this.fb.group({
              id: [null],
              transactionType: ['', Validators.required],
              value: ['', Validators.required],
              accountId: ['', Validators.required]
        });
  }

  ngOnInit() {
    this.cargarTransactions();
    this.getAccounts();
    this.transactionForm.get('id')?.valueChanges
              .pipe(debounceTime(300))
              .subscribe(value => this.filtrarTransactions(value));
  }

  cargarTransactions():void {
    this.transactionService.getTransaction().subscribe({
      next: transactions => {
        this.transactions = transactions;
      },
      error: (err) => {
        this.mensajeError = err.message;
        console.error('Error al cargar movimientos:', err)
      }
    });
  }

  getAccounts(): void {
    this.accountService.getAccounts().subscribe({
      next: accounts => {
        this.accounts = accounts.filter(account => account.state === 'ACTIVA'); // Filtrar solo clientes activos
        console.log(this.accounts);
      },
      error: err => console.error('Error al cargar cuentas:', err)
    });
  }

  filtrarTransactions(termino: string): void {
    const idBuscado = Number(termino)
    this.transactionsFiltradas = this.transactions.filter(transaction =>
      transaction.id === idBuscado
    );
  }

  nuevaTransaction(): void {
      this.transactionForm.reset();
      this.formularioVisible = true;
      this.editando = false;
    }
  
    updateTransaction(transacción: Transaction): void {
      this.transactionForm.patchValue(transacción);
      this.formularioVisible = true;
      this.editando = true;
    }

  deleteTransaction(id: number) {
    if (confirm('¿Estás seguro de eliminar esta transacción?')) {
      this.transactionService.deleteTransaction(id).subscribe({
        next: () => {
          alert('Transacción eliminada con éxito');
          this.cargarTransactions();
        },
        error: (err) => {
          this.mensajeError = err.message;
          console.error('Error al eliminar movimiento:', err);
        }
      });
    }
  }


  guardarTransaction(): void {
    if (this.transactionForm.invalid) return;
    const transaction = this.transactionForm.value;
    this.mensajeError = null; 
    if (this.editando) {
      this.transactionService.updateTransaction(transaction.id, transaction).subscribe({
        next: () => {
          this.cargarTransactions();
          alert('Transacción actualizada con éxito');
          this.transactionForm.reset();
        },
        error: (err) => {
          this.mensajeError = err.message;
          console.error('Error al actualizar movimiento:', err)
        }
      });
    } else {
      this.transactionService.createTransaction(transaction).subscribe({
        next: () => {
          this.cargarTransactions();
          alert('Transacción realizada con éxito');
          this.transactionForm.reset();
        },
        error: (err) => {
          this.mensajeError = err.message;
          console.error('Error al crear movimiento:', err)
        } 
      });
    }
    this.formularioVisible = false;
  }

  cancelarFormulario(): void {
    this.formularioVisible = false; // Ocultar el formulario
    this.transactionForm.reset(); // Opcional: resetear el formulario
  }

  getAccountNumber(id: number): string {
    const account = this.accounts.find(c => c.id === id);
    return account ? account.accountNumber : 'Desconocido';
  }
}
