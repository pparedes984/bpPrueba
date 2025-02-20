import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { Account } from '../models/account.module';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CuentasService {
  private apiUrl = `${environment.API_URL}cuentas`;

  constructor(private http: HttpClient) {}

  getAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(this.apiUrl).pipe(
          catchError(error => {
            return throwError(() => new Error(error.error?.message || 'Error al procesar la transacción'));
          })
        );
  }

  createAccount(account: Account): Observable<Account> {
    return this.http.post<Account>(this.apiUrl, account).pipe(
      catchError(error => {
        return throwError(() => new Error(error.error?.message || 'Error al procesar la transacción'));
      })
    );
  }

  updateAccount(id: number, account: Account): Observable<Account> {
    return this.http.put<Account>(`${this.apiUrl}/${id}`, account).pipe(
      catchError(error => {
        return throwError(() => new Error(error.error?.message || 'Error al procesar la transacción'));
      })
    );
  }

  deleteAccount(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(error => {
        return throwError(() => new Error(error.error?.message || 'Error al procesar la transacción'));
      })
    );
  }
}
