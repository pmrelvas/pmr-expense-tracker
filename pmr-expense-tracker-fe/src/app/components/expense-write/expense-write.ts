import { Component, OnInit, inject, signal } from '@angular/core'
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms'
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatInputModule } from '@angular/material/input'
import { MatSelectModule } from '@angular/material/select'
import { MatButtonModule } from '@angular/material/button'
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { ExpenseService } from '../../services/expense.service'
import { ToastService } from '../../services/toast.service'
import { Expense, ExpensePayload } from '../../models/expense.model'
import { Category } from '../../models/category.model'

export interface ExpenseWriteDialogData {
  expense: Expense | null
  categories: Category[]
}

@Component({
  selector: 'app-expense-write',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './expense-write.html',
  styleUrl: './expense-write.scss'
})
export class ExpenseWriteComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder)
  private readonly expenseService = inject(ExpenseService)
  private readonly toastService = inject(ToastService)
  private readonly dialogRef = inject(MatDialogRef<ExpenseWriteComponent>)
  readonly data = inject<ExpenseWriteDialogData>(MAT_DIALOG_DATA)

  readonly isEditMode = signal(false)
  readonly isSubmitting = signal(false)

  readonly form = this.formBuilder.nonNullable.group({
    amount: [null as number | null, [Validators.required]],
    totalBalance: [null as number | null],
    description: ['', [Validators.required]],
    operationDate: ['', [Validators.required]],
    transactionDate: ['', [Validators.required]],
    source: [''],
    categoryId: [null as number | null]
  })

  readonly categoryOptions = this.data.categories

  ngOnInit(): void {
    const expense = this.data.expense
    if (expense) {
      this.isEditMode.set(true)
      this.form.patchValue({
        amount: expense.amount,
        totalBalance: expense.totalBalance,
        description: expense.description,
        operationDate: this.toDateTimeLocal(expense.operationDate),
        transactionDate: this.toDateTimeLocal(expense.transactionDate),
        source: expense.source ?? '',
        categoryId: expense.category?.id ?? null
      })
    }
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched()
      return
    }

    const { amount, totalBalance, description, operationDate, transactionDate, source, categoryId } =
      this.form.getRawValue()
    const payload: ExpensePayload = {
      amount: amount as number,
      totalBalance,
      description,
      operationDate: this.toIsoDateTime(operationDate),
      transactionDate: this.toIsoDateTime(transactionDate),
      source: source ? source : null,
      categoryId
    }
    const expense = this.data.expense

    this.isSubmitting.set(true)
    const request$ = expense
      ? this.expenseService.update(expense.id, payload)
      : this.expenseService.create(payload)

    request$.subscribe({
      next: () => {
        this.isSubmitting.set(false)
        this.toastService.success(expense ? 'Expense updated' : 'Expense created')
        this.dialogRef.close(true)
      },
      error: () => {
        this.isSubmitting.set(false)
        this.toastService.error('An error has occurred')
      }
    })
  }

  cancel(): void {
    this.dialogRef.close(false)
  }

  private toDateTimeLocal(value: string): string {
    return value ? value.slice(0, 16) : ''
  }

  private toIsoDateTime(value: string): string {
    if (!value) {
      return value
    }
    return value.length === 16 ? `${value}:00` : value
  }
}
