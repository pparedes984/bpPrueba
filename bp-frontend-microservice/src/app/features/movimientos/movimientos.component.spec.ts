import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovimientosComponent } from './movimientos.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MovimientosService } from '../../core/services/movimientos.service';

describe('MovimientosComponent', () => {
  let component: MovimientosComponent;
  let fixture: ComponentFixture<MovimientosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovimientosComponent, HttpClientTestingModule],
      providers : [ MovimientosService ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovimientosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
