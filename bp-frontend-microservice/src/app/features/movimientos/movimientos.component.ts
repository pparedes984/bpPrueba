import { Component, OnInit } from '@angular/core';
import { Transaction } from '../../core/models/transaction.module';
import { CommonModule } from '@angular/common';
import { MovimientosService } from '../../core/services/movimientos.service';
import { transition } from '@angular/animations';

@Component({
  selector: 'app-movimientos',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './movimientos.component.html',
  styleUrl: './movimientos.component.scss'
})
export class MovimientosComponent implements OnInit{
  transactions: Transaction[] = [];
  transactionUpdated: Transaction | null = null;

  constructor(private transactionService: MovimientosService) {}

  ngOnInit() {
    this.cargarTransactions();
  }

  cargarTransactions() {
    this.transactionService.getTransaction().subscribe(data => {
      console.log(data);
      this.transactions = data;
    });
  }

  deleteTransaction(id: number) {
    if (confirm('¿Estás seguro de eliminar esta transacción?')) {
      this.transactionService.deleteTransaction(id).subscribe(() => {
        this.transactions = this.transactions.filter(transaction => transaction.id !== id);
      });
    }
  }

  updateTransaction(transaction: Transaction) {
    this.transactionUpdated = { ...transaction };
  }

  guardarEdicion() {
    if (this.transactionUpdated) {
      this.transactionService.updateTransaction(this.transactionUpdated.id, this.transactionUpdated)
        .subscribe(() => {
          this.cargarTransactions();
          this.transactionUpdated = null;
        });
    }
  }
}
