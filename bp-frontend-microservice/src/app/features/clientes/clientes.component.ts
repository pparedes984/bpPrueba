import { Component } from '@angular/core';
import { ClientesService } from '../../core/services/clientes.service';
import { Cliente } from '../../core/models/cliente.module';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule], // Agregar esta línea
  templateUrl: './clientes.component.html',
  styleUrls: ['./clientes.component.scss']
})
export class ClientesComponent {
  clientes: Cliente[] = [];
  clienteEditando: Cliente | null = null;

  constructor(private clientesService: ClientesService) {}

  ngOnInit() {
    this.cargarClientes();
  }

  cargarClientes() {
    this.clientesService.getClientes().subscribe(data => {
      console.log(data);
      this.clientes = data;
    });
  }

  deleteCliente(id: number) {
    if (confirm('¿Estás seguro de eliminar este cliente?')) {
      this.clientesService.deleteCliente(id).subscribe(() => {
        this.clientes = this.clientes.filter(cliente => cliente.id !== id);
      });
    }
  }

  updateCliente(cliente: Cliente) {
    this.clienteEditando = { ...cliente };
  }

  guardarEdicion() {
    if (this.clienteEditando) {
      this.clientesService.updateCliente(this.clienteEditando.id, this.clienteEditando)
        .subscribe(() => {
          this.cargarClientes();
          this.clienteEditando = null;
        });
    }
  }
}
