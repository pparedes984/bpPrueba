import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { ApplicationConfig, importProvidersFrom } from '@angular/core';
 // Asegúrate de que la configuración esté importada
import { BrowserModule } from '@angular/platform-browser';
import { appConfig } from './app/app.config';

platformBrowserDynamic().bootstrapModule(appConfig, {
  providers: [
    importProvidersFrom(BrowserModule)
  ]
}).catch(err => console.error(err));
