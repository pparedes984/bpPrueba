import { Component } from '@angular/core';
import { jsPDF } from 'jspdf';

@Component({
  selector: 'app-reportes',
  templateUrl: './reportes.component.html',
  standalone: true,
})
export class ReportesComponent {
  generarPDF() {
    const doc = new jsPDF();
    doc.text('Reporte de Movimientos', 10, 10);
    doc.save('reporte.pdf');
  }
}
