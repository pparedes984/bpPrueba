import { Component, OnInit } from '@angular/core';
import { Account } from '../../core/models/account.module';
import { CuentasService } from '../../core/services/cuentas.service';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { debounceTime, Subscription } from 'rxjs';
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
  mensajeError: string | null = null;
  private subscriptions: Subscription = new Subscription();

  constructor(
    private accountService: CuentasService, 
    private fb: FormBuilder,
    private clientesService: ClientesService
  ) {
    this.accountForm = this.fb.group({
          id: [null],
          accountNumber: ['', Validators.required],
          accountType: ['', Validators.required],
          balance: ['', Validators.required],
          state: ['', Validators.required],
          clientId: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.cargarAccounts();
    this.cargarClientes();
    const idChangeSub = this.accountForm.get('id')?.valueChanges
          .pipe(debounceTime(300))
          .subscribe(value => this.filtrarAccounts(value));

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
        this.clients = clientes.filter(cliente => cliente.state === 'ACTIVO'); // Filtrar solo clientes activos
      },
      error: (err) => {
        this.mensajeError = err.message;
        console.error('Error al cargar clientes:', err);
      }
    });
    this.subscriptions.add(loadSub)
  }

  cargarAccounts():void {
    const loadSub = this.accountService.getAccounts().subscribe({
      next: accounts => {
        this.accounts = accounts;
        this.accountsFiltradas = [...accounts];
      },
      error: (err) => {
        this.mensajeError = err.message;
        console.error('Error al cargar cuentas:', err);
      }
    });
    this.subscriptions.add(loadSub)
  }

  filtrarAccounts(termino: string): void {
    if (!termino) {
      this.accountsFiltradas = [...this.accounts]
      return;
    } 

    const idBuscado = Number(termino);
      this.accountsFiltradas = this.accounts.filter(account =>
        account.id === idBuscado);
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
      const deleteSub = this.accountService.deleteAccount(id).subscribe({
        next: () => {
          this.cargarAccounts();
          alert("Cuenta elminada exitosamente");
        },
        error: (err) => {
          this.mensajeError = err.message;
          console.error('Error al eliminar cuenta:', err);

        }
      });
      this.subscriptions.add(deleteSub);
    }
  }

  guardarAccount(): void {
    if (this.accountForm.invalid) return;
    const account = this.accountForm.value;
    this.mensajeError = null; 
    const saveSub = (this.editando ? this.accountService.updateAccount(account.id, account) : this.accountService.createAccount(account))
      .subscribe({
        next: () => {
          this.cargarAccounts();
          alert(this.editando ? 'Cuenta actualizado con éxito' : 'Cuenta guardado con éxito');
          this.accountForm.reset();
        },
        error: (err) => {
          this.mensajeError = err.message;
          console.error(`Error al ${this.editando ? 'actualizar' : 'crear'} cuenta:`, err);
        }
      });
    
    this.subscriptions.add(saveSub);
    this.formularioVisible = false;
  }
  // Método para cancelar el formulario
  cancelarFormulario(): void {
    this.formularioVisible = false; // Ocultar el formulario
    this.accountForm.reset();
    this.cargarAccounts(); // Opcional: resetear el formulario
  }

  obtenerNombreCliente(id: number): string {
    const cliente = this.clients.find(c => c.id === id);
    return cliente ? cliente.name : 'Desconocido';
  }
}

