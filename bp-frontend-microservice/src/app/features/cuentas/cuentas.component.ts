import { Component, OnInit } from '@angular/core';
import { Account } from '../../core/models/account.module';
import { CuentasService } from '../../core/services/cuentas.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cuentas',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './cuentas.component.html',
  styleUrl: './cuentas.component.scss'
})
export class CuentasComponent implements OnInit{
  accounts: Account[] = [];
  accountUpdated: Account | null = null;

  constructor(private accountService: CuentasService) {}

  ngOnInit() {
    this.cargarAccounts();
  }

  cargarAccounts() {
    this.accountService.getAccounts().subscribe(data => {
      console.log(data);
      this.accounts = data;
    });
  }

  deleteAccount(id: number) {
    if (confirm('¿Estás seguro de eliminar esta cuenta?')) {
      this.accountService.deleteAccount(id).subscribe(() => {
        this.accounts = this.accounts.filter(account => account.id !== id);
      });
    }
  }

  updateAccount(account: Account) {
    this.accountUpdated = { ...account };
  }

  guardarEdicion() {
    if (this.accountUpdated) {
      this.accountService.updateAccount(this.accountUpdated.id, this.accountUpdated)
        .subscribe(() => {
          this.cargarAccounts();
          this.accountUpdated = null;
        });
    }
  }

}
