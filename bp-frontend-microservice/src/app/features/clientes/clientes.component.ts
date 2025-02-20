import { Component, OnInit } from '@angular/core';
import { ClientesService } from '../../core/services/clientes.service';
import { Client } from '../../core/models/client.module';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { debounceTime, Subscription } from 'rxjs';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule], // Agregar esta línea
  templateUrl: './clientes.component.html',
  styleUrls: ['./clientes.component.scss']
})
export class ClientesComponent implements OnInit{
  clients: Client[] = [];
  clientesFiltrados: Client[] = [];
  formularioVisible = false;
  editando = false;
  filtroId = '';
  clienteForm: FormGroup;
  mensajeError: string | null = null;
  private subscriptions: Subscription = new Subscription();

  constructor(
    private clientesService: ClientesService, 
    private fb: FormBuilder
  ) {
    this.clienteForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      dni: ['', [Validators.required, Validators.pattern('^[0-9]+$')]],
      telephone: ['', Validators.required],
      address: ['', Validators.required],
      gender: ['', Validators.required],
      age: ['', [Validators.required, Validators.min(1)]],
      state: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.cargarClientes();
    const idChangeSub = this.clienteForm.get('id')?.valueChanges
      .pipe(debounceTime(300))
      .subscribe(value => this.filtrarClientes(value));

    if (idChangeSub){
      this.subscriptions.add(idChangeSub);
    }
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  cargarClientes(): void {
    const loadSub = this.clientesService.getClients().subscribe({
      next: clientes => {
        this.clients = clientes;
        this.clientesFiltrados = [...clientes];
      },
      error: (err) => {
        this.mensajeError = err.message;
        console.error('Error al cargar clientes:', err);
      }
    });
    this.subscriptions.add(loadSub)
  }

  filtrarClientes(termino: string): void {
    if (!termino) {
      this.clientesFiltrados = [...this.clients]; // Mostrar todos si el campo está vacío
      return;
    }
  
    const idBuscado = Number(termino);
    this.clientesFiltrados = this.clients.filter(cliente => 
      cliente.id === idBuscado);
  }
  

  nuevoCliente(): void {
    this.clienteForm.reset();
    this.formularioVisible = true;
    this.editando = false;
  }

  updateClient(cliente: Client): void {
    this.clienteForm.patchValue(cliente);
    this.formularioVisible = true;
    this.editando = true;
  }

  deleteClient(id: number): void {
    if (confirm('¿Está seguro de eliminar este cliente?')) {
      const deleteSub = this.clientesService.deleteClient(id).subscribe({
        next: () => {
          alert('Cliente eliminado con éxito');
          this.cargarClientes();
        },
        error: (err) => {
          this.mensajeError = err.message;
          console.error('Error al eliminar cliente:', err)
        }
      });
      this.subscriptions.add(deleteSub);
    }
  }

  guardarCliente(): void {
    if (this.clienteForm.invalid) return;
    const cliente = this.clienteForm.value;
    this.mensajeError = null; 
    const saveSub = (this.editando ? this.clientesService.updateClient(cliente.id, cliente) : this.clientesService.createClient(cliente))
      .subscribe({
        next: () => {
          this.cargarClientes();
          alert(this.editando ? 'Cliente actualizado con éxito' : 'Clienta guardado con éxito');
          this.clienteForm.reset();
        },
        error: (err) => {
          this.mensajeError = err.message;
          console.error(`Error al ${this.editando ? 'actualizar' : 'crear'} cliente:`, err);
        }
      });

    this.subscriptions.add(saveSub);
    this.formularioVisible = false;
  }
  // Método para cancelar el formulario
  cancelarFormulario(): void {
    this.cargarClientes();
    this.formularioVisible = false;
    this.clienteForm.reset(); // Opcional: resetear el formulario
  }

}
