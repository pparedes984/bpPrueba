import { Component } from '@angular/core';
import { jsPDF } from 'jspdf';
import { ReportesService } from '../../core/services/reportes.service';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ReportResponse } from '../../core/models/reportResponse.module';

@Component({
  selector: 'app-reportes',
  templateUrl: './reportes.component.html',
  standalone: true,
  styleUrl: './reportes.component.scss',
  imports: [CommonModule, ReactiveFormsModule, FormsModule]
})
export class ReportesComponent {
  startDate: string = '';
  endDate: string = '';
  reportData: ReportResponse | null = null ;

  constructor(private reportesService: ReportesService) {}

  consultarReporte() {
    if (!this.startDate || !this.endDate) {
      alert('Por favor, selecciona ambas fechas.');
      return;
    }

    this.reportesService.obtenerReporte(this.startDate, this.endDate)
    .subscribe({
      next: (data) => {
        this.reportData = {
          ...data,
          startDate: new Date(data.startDate),
          endDate: new Date(data.endDate)
        };
      },
      error: (error) => {
        console.error('Error al obtener el reporte', error);
        alert('Hubo un error al obtener el reporte.');
      }
    });
  }

  generarPDF() {
    if (!this.reportData || !this.reportData.accountReports.length) {
      alert('Primero consulta el reporte.');
      return;
    }

    this.reportesService.descargarPDF(this.startDate, this.endDate);
  }
}
