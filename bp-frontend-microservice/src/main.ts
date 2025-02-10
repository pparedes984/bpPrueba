import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config';
// Importa el componente principal

bootstrapApplication(AppComponent, appConfig) // Primero AppComponent, luego appConfig
  .catch(err => console.error(err));
