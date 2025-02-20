import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ReportResponse } from '../models/reportResponse.module';

@Injectable({
  providedIn: 'root'
})
export class ReportesService {

  private apiUrl = `${environment.API_URL}reportes`;

  constructor(private http: HttpClient) {}

  obtenerReporte(startDate: string, endDate: string): Observable<ReportResponse> {
    const formattedStartDate = `${startDate}T00:00:00`; // Aseguramos el formato correcto
    const formattedEndDate = `${endDate}T23:59:59`; // Ajustamos para incluir todo el día
    return this.http.get<ReportResponse>(`${this.apiUrl}?startDate=${formattedStartDate}&endDate=${formattedEndDate}`)
    .pipe(
      map(response => ({
        ...response,
        startDate: new Date(response.startDate),
        endDate: new Date(response.endDate)
      }))
    );
  }
  

  descargarPDF(startDate: string, endDate: string) {
    const formattedStartDate = `${startDate}T00:00:00`;
    const formattedEndDate = `${endDate}T23:59:59`;

    const url = `${this.apiUrl}/pdf?startDate=${formattedStartDate}&endDate=${formattedEndDate}`;
    const headers = new HttpHeaders({ 'Accept': 'application/pdf' });

    this.http.get(url, { headers, responseType: 'blob' }).subscribe(response => {
      const blob = new Blob([response], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'Reporte.pdf';
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
    }, error => {
      console.error('Error al descargar el PDF', error);
      alert('Hubo un error al descargar el PDF.');
    });
  }
}
