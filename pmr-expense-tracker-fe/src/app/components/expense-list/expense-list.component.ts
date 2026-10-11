import { Component, OnInit, signal, inject, ChangeDetectionStrategy } from '@angular/core'

import { MatTableModule } from '@angular/material/table'
import { MatButtonModule } from '@angular/material/button'
import { MatCardModule } from '@angular/material/card'
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { MatChipsModule } from '@angular/material/chips'
import { MatIconModule } from '@angular/material/icon'
import { MatDialog } from '@angular/material/dialog'
import { ExpenseService } from '../../services/expense.service'
import { CategoryService } from '../../services/category.service'
import { Expense } from '../../models/expense.model'
import { Category } from '../../models/category.model'
import { ExpenseWriteComponent } from '../expense-write/expense-write'
import { ConfirmDialogComponent, ConfirmDialogData } from '../confirm-dialog/confirm-dialog'
import { ToastService } from '../../services/toast.service'

@Component({
  selector: 'app-expense-list',
  standalone: true,
  imports: [
    MatTableModule,
    MatButtonModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    MatIconModule
],
  templateUrl: './expense-list.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './expense-list.component.scss'
})
export class ExpenseListComponent implements OnInit {
  private readonly expenseService = inject(ExpenseService)
  private readonly categoryService = inject(CategoryService)
  private readonly dialog = inject(MatDialog);
  private readonly toastService = inject(ToastService)

  expenses = signal<Expense[]>([])
  categories = signal<Category[]>([])
  isLoading = signal(false)
  error = signal<string | null>(null)

  displayedColumns: string[] = ['id', 'amount', 'description', 'operationDate', 'transactionDate', 'source', 'category', 'createdAt', 'updatedAt', 'actions']

  ngOnInit(): void {
    this.loadExpenses()
    this.loadCategories()
  }

  loadExpenses(): void {
    this.isLoading.set(true)
    this.error.set(null)

    this.expenseService.getAll().subscribe({
      next: (data) => {
        this.expenses.set(data)
        this.isLoading.set(false)
      },
      error: (err) => {
        this.error.set(err.message || 'Failed to load expenses')
        this.isLoading.set(false)
        console.error('Error loading expenses:', err)
      }
    })
  }

  private loadCategories(): void {
    this.categoryService.getAll().subscribe({
      next: (data) => this.categories.set(data),
      error: (err) => console.error('Error loading categories:', err)
    })
  }

  addExpense(): void {
    this.openExpenseDialog(null)
  }

  updateExpense(expense: Expense): void {
    this.openExpenseDialog(expense)
  }

  deleteExpense(expense: Expense): void {
    const dialogRef = this.dialog.open<ConfirmDialogComponent, ConfirmDialogData, boolean>(
      ConfirmDialogComponent,
      {
        width: '400px',
        data: {
          title: 'Delete expense',
          message: `Are you sure you want to delete the expense "${expense.description}"? This action cannot be undone.`,
          confirmLabel: 'Delete',
          cancelLabel: 'Cancel',
          confirmColor: 'warn'
        }
      }
    )

    dialogRef.afterClosed().subscribe((confirmed) => {
      if (!confirmed) {
        return
      }

      this.expenseService.delete(expense.id).subscribe({
        next: () => {
          this.toastService.success('Expense deleted')
          this.loadExpenses()
        },
        error: () => {
          this.toastService.error('An error has occurred')
        }
      })
    })
  }

  private openExpenseDialog(expense: Expense | null): void {
    const dialogRef = this.dialog.open(ExpenseWriteComponent, {
      width: '520px',
      data: {
        expense,
        categories: this.categories()
      }
    })

    dialogRef.afterClosed().subscribe((saved) => {
      if (saved) {
        this.loadExpenses()
      }
    })
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleString()
  }

  formatAmount(amount: number): string {
    return new Intl.NumberFormat(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 }).format(amount)
  }
}
