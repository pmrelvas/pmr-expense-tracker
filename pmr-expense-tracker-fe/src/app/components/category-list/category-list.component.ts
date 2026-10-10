import { Component, OnInit, signal, inject, ChangeDetectionStrategy } from '@angular/core'

import { MatTableModule } from '@angular/material/table'
import { MatButtonModule } from '@angular/material/button'
import { MatCardModule } from '@angular/material/card'
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { MatChipsModule } from '@angular/material/chips'
import { MatIconModule } from '@angular/material/icon'
import { MatDialog } from '@angular/material/dialog'
import { CategoryService } from '../../services/category.service'
import { Category } from '../../models/category.model'
import { CategoryWriteComponent } from '../category-write/category-write'

@Component({
  selector: 'app-category-list',
  standalone: true,
  imports: [
    MatTableModule,
    MatButtonModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    MatIconModule
],
  templateUrl: './category-list.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './category-list.component.scss'
})
export class CategoryListComponent implements OnInit {
  private readonly categoryService = inject(CategoryService)
  private readonly dialog = inject(MatDialog);

  categories = signal<Category[]>([])
  isLoading = signal(false)
  error = signal<string | null>(null)

  displayedColumns: string[] = ['id', 'code', 'name', 'parentCategory', 'createdAt', 'updatedAt', 'actions']

  ngOnInit(): void {
    this.loadCategories()
  }

  loadCategories(): void {
    this.isLoading.set(true)
    this.error.set(null)

    this.categoryService.getAll().subscribe({
      next: (data) => {
        this.categories.set(data)
        this.isLoading.set(false)
      },
      error: (err) => {
        this.error.set(err.message || 'Failed to load categories')
        this.isLoading.set(false)
        console.error('Error loading categories:', err)
      }
    })
  }

  addCategory(): void {
    this.openCategoryDialog(null)
  }

  updateCategory(category: Category): void {
    this.openCategoryDialog(category)
  }

  private openCategoryDialog(category: Category | null): void {
    const dialogRef = this.dialog.open(CategoryWriteComponent, {
      width: '480px',
      data: {
        category,
        categories: this.categories()
      }
    })

    dialogRef.afterClosed().subscribe((saved) => {
      if (saved) {
        this.loadCategories()
      }
    })
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleString()
  }
}
