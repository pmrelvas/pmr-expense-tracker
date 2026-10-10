import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CategoryWriteComponent } from './category-write';

describe('CategoryWrite', () => {
  let component: CategoryWriteComponent;
  let fixture: ComponentFixture<CategoryWriteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategoryWriteComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: MAT_DIALOG_DATA, useValue: { category: null, categories: [] } },
        { provide: MatDialogRef, useValue: { close: () => undefined } }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CategoryWriteComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
