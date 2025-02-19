import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Transaction } from '../models/transaction.module';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MovimientosService {
  private apiUrl = `${environment.API_URL}movimientos`;

  constructor(private http: HttpClient) {}

  getTransaction(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(this.apiUrl).pipe(
      catchError(error => {
        return throwError(() => new Error(error.error?.message || 'Error al procesar la transacción'));
      })
    );
  }

  createTransaction(transaction: Transaction): Observable<Transaction> {
    return this.http.post<Transaction>(`${this.apiUrl}/${transaction.accountId}`, transaction).pipe(
      catchError(error => {
        return throwError(() => new Error(error.error?.message || 'Error al procesar la transacción'));
      })
    );
  }

  updateTransaction(id: number, transaction: Transaction): Observable<Transaction> {
    return this.http.put<Transaction>(`${this.apiUrl}/${id}`, transaction).pipe(
      catchError(error => {
        return throwError(() => new Error(error.error?.message || 'Error al procesar la transacción'));
      })
    );
  }

  deleteTransaction(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(error => {
        return throwError(() => new Error(error.error?.message || 'Error al procesar la transacción'));
      })
    );
  }
}
