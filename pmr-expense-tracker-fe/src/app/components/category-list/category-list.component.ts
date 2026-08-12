import { Component, OnInit, signal, inject } from '@angular/core'
import { CommonModule } from '@angular/common'
import { MatTableModule } from '@angular/material/table'
import { MatButtonModule } from '@angular/material/button'
import { MatCardModule } from '@angular/material/card'
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { MatChipsModule } from '@angular/material/chips'
import { CategoryService } from '../../services/category.service'
import { Category } from '../../models/category.model'

@Component({
  selector: 'app-category-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatChipsModule
  ],
  templateUrl: './category-list.component.html',
  styleUrl: './category-list.component.scss'
})
export class CategoryListComponent implements OnInit {
  private readonly categoryService = inject(CategoryService)

  categories = signal<Category[]>([])
  isLoading = signal(false)
  error = signal<string | null>(null)

  displayedColumns: string[] = ['id', 'code', 'name', 'parentCategory', 'createdAt', 'updatedAt']

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

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleString()
  }
}

