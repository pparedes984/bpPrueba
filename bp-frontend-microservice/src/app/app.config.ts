import { ApplicationConfig, importProvidersFrom, provideZoneChangeDetection } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

export const appConfig: ApplicationConfig = {
  providers: [
    importProvidersFrom(FormsModule, ReactiveFormsModule),
    provideZoneChangeDetection({ eventCoalescing: true }), provideAnimationsAsync(), provideHttpClient(),
    provideRouter([
      { path: 'clientes', loadComponent: ()=> import('./features/clientes/clientes.component').then( c => c.ClientesComponent ) },
      { path: 'cuentas', loadComponent: ()=> import('./features/cuentas/cuentas.component').then( c => c.CuentasComponent ) },
      { path: 'movimientos', loadComponent: ()=> import('./features/movimientos/movimientos.component').then( c => c.MovimientosComponent ) },
      { path: 'reportes', loadComponent: ()=> import('./features/reportes/reportes.component').then( c => c.ReportesComponent ) },
      { path: '', redirectTo: '/clientes', pathMatch: 'full' } // Redirección por defecto
    ], withComponentInputBinding()) // Necesario para standalone components
  ]
};
