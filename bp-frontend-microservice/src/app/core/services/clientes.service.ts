import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Client } from '../models/client.module';

@Injectable({
  providedIn: 'root',
})
export class ClientesService {
  private apiUrl = `${environment.API_URL}clientes`;

  constructor(private http: HttpClient) {}

  getClients(): Observable<Client[]> {
    return this.http.get<Client[]>(this.apiUrl).pipe(
      catchError(error => {
        return throwError(() => new Error(error.error?.message || 'Error al procesar la transacción'));
      })
    );
  }

  createClient(cliente: Client): Observable<Client> {
    return this.http.post<Client>(this.apiUrl, cliente).pipe(
      catchError(error => {
        return throwError(() => new Error(error.error?.message || 'Error al procesar la transacción'));
      })
    );
  }

  updateClient(id: number, cliente: Client): Observable<Client> {
    return this.http.put<Client>(`${this.apiUrl}/${id}`, cliente).pipe(
      catchError(error => {
        return throwError(() => new Error(error.error?.message || 'Error al procesar la transacción'));
      })
    );
  }

  deleteClient(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(error => {
        return throwError(() => new Error(error.error?.message || 'Error al procesar la transacción'));
      })
    );
  }
}
