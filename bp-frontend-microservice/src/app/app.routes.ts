import { Routes } from '@angular/router';
import { ClientesComponent } from './features/clientes/clientes.component';
import { CuentasComponent } from './features/cuentas/cuentas.component';
import { MovimientosComponent } from './features/movimientos/movimientos.component';
import { ReportesComponent } from './features/reportes/reportes.component';

export const routes: Routes = [
  { path: '', component: ClientesComponent, children: [
    { path: 'clientes', component: ClientesComponent },
    { path: 'cuentas', component: CuentasComponent },
    { path: 'movimientos', component: MovimientosComponent },
    { path: 'reportes', component: ReportesComponent }
  ] }
];
