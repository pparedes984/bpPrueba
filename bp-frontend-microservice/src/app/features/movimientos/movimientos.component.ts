import { Component, OnInit } from '@angular/core';
import { Transaction } from '../../core/models/transaction.module';
import { CommonModule } from '@angular/common';
import { MovimientosService } from '../../core/services/movimientos.service';
import { transition } from '@angular/animations';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { debounceTime } from 'rxjs';

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

  constructor(private transactionService: MovimientosService, private fb: FormBuilder) {
    this.transactionForm = this.fb.group({
              id: [null],
              date: ['', Validators.required],
              transactionType: ['', Validators.required],
              value: ['', Validators.required],
              balance: ['', Validators.required],
              accountId: ['', Validators.required]
        });
  }

  ngOnInit() {
    this.cargarTransactions();
    this.transactionForm.get('id')?.valueChanges
              .pipe(debounceTime(300))
              .subscribe(value => this.filtrarTransactions(value));
  }

  cargarTransactions():void {
    this.transactionService.getTransaction().subscribe({
      next: transactions => {
        this.transactions = transactions;
      },
      error: err => console.error('Error al cargar movimientos:', err)
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
        next: () => this.cargarTransactions(),
        error: err => console.error('Error al eliminar movimiento:', err)
      });
    }
  }


  guardarTransaction(): void {
    if (this.transactionForm.invalid) return;
    const transaction = this.transactionForm.value;
    if (this.editando) {
      this.transactionService.updateTransaction(transaction.id, transaction).subscribe({
        next: () => this.cargarTransactions(),
        error: err => console.error('Error al actualizar movimiento:', err)
      });
    } else {
      this.transactionService.createTransaction(transaction).subscribe({
        next: () => this.cargarTransactions(),
        error: err => console.error('Error al crear movimiento:', err)
      });
    }
    this.formularioVisible = false;
  }

  cancelarFormulario(): void {
    this.formularioVisible = false; // Ocultar el formulario
    this.transactionForm.reset(); // Opcional: resetear el formulario
  }
}
