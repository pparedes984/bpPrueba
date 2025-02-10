import { ApplicationConfig } from '@angular/core';
import { provideRouter, RouterModule, Routes } from '@angular/router';
import { importProvidersFrom } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
import { NgModule } from '@angular/core';

// Componentes principales
import { ClientesComponent } from './features/clientes/clientes.component';
import { LayoutComponent } from './shared/layout/layout.component';
import { SidebarComponent } from './shared/sidebar/sidebar.component';

// Definir rutas
const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,  // Aplicamos el layout
    children: [
      { path: 'clientes', component: ClientesComponent },
      { path: '', redirectTo: 'clientes', pathMatch: 'full' }
    ]
  }
];

export const appConfig: ApplicationConfig = {
  providers: [
    RouterModule,
    provideHttpClient(),
    importProvidersFrom(CommonModule, FormsModule),
  ]
};
