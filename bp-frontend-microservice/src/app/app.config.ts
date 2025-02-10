import { ApplicationConfig, importProvidersFrom, provideZoneChangeDetection } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { ClientesComponent } from './features/clientes/clientes.component';
import { CuentasComponent } from './features/cuentas/cuentas.component';
import { MovimientosComponent } from './features/movimientos/movimientos.component';
import { ReportesComponent } from './features/reportes/reportes.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

export const appConfig: ApplicationConfig = {
  providers: [
    importProvidersFrom(FormsModule),
    provideZoneChangeDetection({ eventCoalescing: true }), provideAnimationsAsync(), provideHttpClient(),
    provideRouter([
      { path: 'clientes', component: ClientesComponent },
      { path: 'cuentas', component: CuentasComponent },
      { path: 'movimientos', component: MovimientosComponent },
      { path: 'reportes', component: ReportesComponent },
      { path: '', redirectTo: '/clientes', pathMatch: 'full' } // Redirección por defecto
    ], withComponentInputBinding()) // Necesario para standalone components
  ]
};
