export interface Category {
  id: number
  code: string
  name: string
  parentCategory: Category | null
  createdAt: string
  updatedAt: string
}

export interface CategoryPayload {
  code: string
  name: string
  parentCategoryId: number | null
}

export interface CategoryFilters {
  code?: string
  name?: string
  'parent-category-id'?: number
}

