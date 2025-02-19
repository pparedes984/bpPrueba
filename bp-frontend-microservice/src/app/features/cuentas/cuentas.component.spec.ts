import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CuentasComponent } from './cuentas.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CuentasService } from '../../core/services/cuentas.service';

describe('CuentasComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, CuentasComponent], // ✅ Agregar módulo para evitar el error de HttpClient
      providers: [CuentasService]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(CuentasComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});
