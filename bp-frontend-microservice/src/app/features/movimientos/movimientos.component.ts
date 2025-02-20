import { Component, OnInit } from '@angular/core';
import { Transaction } from '../../core/models/transaction.module';
import { CommonModule } from '@angular/common';
import { MovimientosService } from '../../core/services/movimientos.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { debounceTime, Subscription } from 'rxjs';
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
  private subscriptions: Subscription = new Subscription();

  constructor(
    private transactionService: MovimientosService, 
    private fb: FormBuilder,
    private accountService: CuentasService
  ) {
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
    const idChangeSub = this.transactionForm.get('id')?.valueChanges
              .pipe(debounceTime(300))
              .subscribe(value => this.filtrarTransactions(value));
    if (idChangeSub){
      this.subscriptions.add(idChangeSub);
    }
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  cargarTransactions():void {
    const loadSub = this.transactionService.getTransaction().subscribe({
      next: transactions => {
        this.transactions = transactions;
        this.transactionsFiltradas = [...transactions]
      },
      error: (err) => {
        this.mensajeError = err.message;
        console.error('Error al cargar movimientos:', err)
      }
    });
    this.subscriptions.add(loadSub)
  }

  getAccounts(): void {
    const loadSub = this.accountService.getAccounts().subscribe({
      next: accounts => {
        this.accounts = accounts.filter(account => account.state === 'ACTIVA'); // Filtrar solo clientes activos
      },
      error: (err) => {
        this.mensajeError = err.message;
        console.error('Error al cargar cuentas:', err);
      }
    });
    this.subscriptions.add(loadSub)
  }

  filtrarTransactions(termino: string): void {
    if(!termino) { 
      this.transactionsFiltradas = [...this.transactions];
      return;
    }
    const idBuscado = Number(termino)
    this.transactionsFiltradas = this.transactions.filter(transaction =>
      Number(transaction.id) === idBuscado);
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
      const deleteSub = this.transactionService.deleteTransaction(id).subscribe({
        next: () => {
          alert('Transacción eliminada con éxito');
          this.cargarTransactions();
        },
        error: (err) => {
          this.mensajeError = err.message;
          console.error('Error al eliminar movimiento:', err);
        }
      });
      this.subscriptions.add(deleteSub);
    }
  }


  guardarTransaction(): void {
    if (this.transactionForm.invalid) return;
    const transaction = this.transactionForm.value;
    this.mensajeError = null; 
    const saveSub = (this.editando ? this.transactionService.updateTransaction(transaction.id, transaction) : this.transactionService.createTransaction(transaction))
    .subscribe({
        next: () => {
          this.cargarTransactions();
          alert(this.editando ? 'Transaccion actualizada con éxito' : 'Transaccion guardado con éxito');
          this.transactionForm.reset();
        },
        error: (err) => {
          this.mensajeError = err.message;
          console.error(`Error al ${this.editando ? 'actualizar' : 'crear'} transaccion:`, err)
        }
      });
    
    this.subscriptions.add(saveSub);
    this.formularioVisible = false;
  }

  cancelarFormulario(): void {
    this.formularioVisible = false; // Ocultar el formulario
    this.transactionForm.reset(); 
    this.cargarTransactions();// Opcional: resetear el formulario
  }

  getAccountNumber(id: number): string {
    const account = this.accounts.find(c => c.id === id);
    return account ? account.accountNumber : 'Desconocido';
  }
}
