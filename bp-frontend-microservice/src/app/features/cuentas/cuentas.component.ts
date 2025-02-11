import { Component, OnInit } from '@angular/core';
import { Account } from '../../core/models/account.module';
import { CuentasService } from '../../core/services/cuentas.service';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { debounceTime } from 'rxjs';
import { Client } from '../../core/models/client.module';
import { ClientesService } from '../../core/services/clientes.service';

@Component({
  selector: 'app-cuentas',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  standalone: true,
  templateUrl: './cuentas.component.html',
  styleUrl: './cuentas.component.scss'
})
export class CuentasComponent implements OnInit{
  accounts: Account[] = [];
  accountsFiltradas: Account[] = [];
  clients: Client[] = [];
  formularioVisible = false;
  editando = false;
  filtroId = '';
  accountForm: FormGroup;

  constructor(
    private accountService: CuentasService, 
    private fb: FormBuilder,
    private clientesService: ClientesService
  ) {
    this.accountForm = this.fb.group({
          id: [null],
          accountNumber: ['', Validators.required],
          accountType: ['', Validators.required],
          openingBalance: ['', Validators.required],
          state: ['', Validators.required],
          clientId: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.cargarAccounts();
    this.cargarClientes();
    this.accountForm.get('id')?.valueChanges
          .pipe(debounceTime(300))
          .subscribe(value => this.filtrarAccounts(value));
  }

  cargarClientes(): void {
    this.clientesService.getClients().subscribe({
      next: clientes => {
        this.clients = clientes.filter(cliente => cliente.state === 'ACTIVO'); // Filtrar solo clientes activos
      },
      error: err => console.error('Error al cargar clientes:', err)
    });
  }

  cargarAccounts():void {
    this.accountService.getAccounts().subscribe({
      next: accounts => {
        this.accounts = accounts;
        this.accountsFiltradas = accounts;
      },
      error: err => console.error('Error al cargar cuentas:', err)
    });
  }

  filtrarAccounts(termino: string): void {
    const idBuscado = Number(termino)
    this.accountsFiltradas = this.accounts.filter(account =>
      account.id === idBuscado
    );
  }

  nuevaAccount(): void {
    this.accountForm.reset();
    this.formularioVisible = true;
    this.editando = false;
  }

  updateAccount(account: Account): void {
    this.accountForm.patchValue(account);
    this.formularioVisible = true;
    this.editando = true;
  }

  deleteAccount(id: number) {
    if (confirm('¿Estás seguro de eliminar esta cuenta?')) {
      this.accountService.deleteAccount(id).subscribe({
        next: () => this.cargarAccounts(),
        error: err => console.error('Error al eliminar cuenta:', err)
      });
    }
  }

  guardarAccount(): void {
    if (this.accountForm.invalid) return;
    const account = this.accountForm.value;
    if (this.editando) {
      this.accountService.updateAccount(account.id, account).subscribe({
        next: () => this.cargarAccounts(),
        error: err => console.error('Error al actualizar cuenta:', err)
      });
    } else {
      this.accountService.createAccount(account).subscribe({
        next: () => this.cargarAccounts(),
        error: err => console.error('Error al crear cuenta:', err)
      });
    }
    this.formularioVisible = false;
  }
  // Método para cancelar el formulario
  cancelarFormulario(): void {
    this.formularioVisible = false; // Ocultar el formulario
    this.accountForm.reset(); // Opcional: resetear el formulario
  }

  obtenerNombreCliente(id: number): string {
    const cliente = this.clients.find(c => c.id === id);
    return cliente ? cliente.name : 'Desconocido';
  }
}

