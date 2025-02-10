import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from './shared/sidebar/sidebar.component';
import { HeaderComponent } from './shared/header/header.component';

// Importar el Sidebar

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  standalone: true, // Indicar que este componente es independiente
  imports: [RouterOutlet, SidebarComponent, HeaderComponent], // Importar SidebarComponent aquí
})
export class AppComponent {
  title = 'bp-frontend-microservice';
}
