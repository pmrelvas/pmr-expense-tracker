import { Category } from './category.model'

export interface Expense {
  id: number
  amount: number
  totalBalance: number | null
  description: string
  operationDate: string
  transactionDate: string
  source: string | null
  category: Category | null
  createdAt: string
  updatedAt: string
}

export interface ExpensePayload {
  amount: number
  totalBalance: number | null
  description: string
  operationDate: string
  transactionDate: string
  source: string | null
  categoryId: number | null
}

export interface ExpenseFilters {
  amount?: number
  operationDate?: string
  transactionDate?: string
  source?: string
  categoryId?: number
}
