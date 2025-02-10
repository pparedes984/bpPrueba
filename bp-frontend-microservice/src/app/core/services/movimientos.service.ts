import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MovimientosService {
private apiUrl = 'http://localhost:8080/api/movimientos';

  constructor(private http: HttpClient) {}

  getTransaction(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  createTransaction(transaction: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, transaction);
  }

  updateTransaction(id: number, transaction: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, transaction);
  }

  deleteTransaction(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
