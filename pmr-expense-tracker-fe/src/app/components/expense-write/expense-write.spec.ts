import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ExpenseWriteComponent } from './expense-write';

describe('ExpenseWrite', () => {
  let component: ExpenseWriteComponent;
  let fixture: ComponentFixture<ExpenseWriteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExpenseWriteComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: MAT_DIALOG_DATA, useValue: { expense: null, categories: [] } },
        { provide: MatDialogRef, useValue: { close: () => undefined } }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ExpenseWriteComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
