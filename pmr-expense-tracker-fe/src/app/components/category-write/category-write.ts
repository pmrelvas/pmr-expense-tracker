import { Component, OnInit, computed, inject, signal } from '@angular/core'
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms'
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatInputModule } from '@angular/material/input'
import { MatSelectModule } from '@angular/material/select'
import { MatButtonModule } from '@angular/material/button'
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { CategoryService } from '../../services/category.service'
import { ToastService } from '../../services/toast.service'
import { Category, CategoryPayload } from '../../models/category.model'

export interface CategoryWriteDialogData {
  category: Category | null
  categories: Category[]
}

@Component({
  selector: 'app-category-write',
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
  templateUrl: './category-write.html',
  styleUrl: './category-write.scss'
})
export class CategoryWriteComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder)
  private readonly categoryService = inject(CategoryService)
  private readonly toastService = inject(ToastService)
  private readonly dialogRef = inject(MatDialogRef<CategoryWriteComponent>)
  readonly data = inject<CategoryWriteDialogData>(MAT_DIALOG_DATA)

  readonly isEditMode = signal(false)
  readonly isSubmitting = signal(false)

  readonly form = this.formBuilder.nonNullable.group({
    code: ['', [Validators.required]],
    name: ['', [Validators.required]],
    parentCategoryId: [null as number | null]
  })

  readonly parentOptions = computed(() => {
    const editingId = this.data.category?.id
    return this.data.categories.filter((category) => category.id !== editingId)
  })

  ngOnInit(): void {
    const category = this.data.category
    if (category) {
      this.isEditMode.set(true)
      this.form.patchValue({
        code: category.code,
        name: category.name,
        parentCategoryId: category.parentCategory?.id ?? null
      })
    }
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched()
      return
    }

    const { code, name, parentCategoryId } = this.form.getRawValue()
    const payload: CategoryPayload = { code, name, parentCategoryId }
    const category = this.data.category

    this.isSubmitting.set(true)
    const request$ = category
      ? this.categoryService.update(category.id, payload)
      : this.categoryService.create(payload)

    request$.subscribe({
      next: () => {
        this.isSubmitting.set(false)
        this.toastService.success(category ? 'Category updated' : 'Category created')
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
}
