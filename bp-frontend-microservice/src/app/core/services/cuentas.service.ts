import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CuentasService {
private apiUrl = 'http://localhost:8080/api/cuentas';

  constructor(private http: HttpClient) {}

  getAccounts(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  createAccount(account: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, account);
  }

  updateAccount(id: number, account: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, account);
  }

  deleteAccount(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
