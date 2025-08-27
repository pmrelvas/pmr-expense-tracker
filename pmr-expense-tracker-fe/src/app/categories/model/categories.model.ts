export interface Category {
  id: number;
  code: string;
  name: string;
  parentCategory: Category;
  createdAt: string;
  updatedAt: string;
}

export interface CategoryFilter {
  code?: string;
  name?: string;
  parentCategoryId?: number;
}
