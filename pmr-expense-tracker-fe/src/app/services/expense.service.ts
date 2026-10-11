import { Injectable, inject } from '@angular/core'
import { HttpClient, HttpParams } from '@angular/common/http'
import { Observable } from 'rxjs'
import { Expense, ExpenseFilters, ExpensePayload } from '../models/expense.model'

@Injectable({
  providedIn: 'root'
})
export class ExpenseService {
  private readonly http = inject(HttpClient)
  private readonly apiUrl = 'http://localhost:8080/api/v1/expenses'

  getAll(filters?: ExpenseFilters): Observable<Expense[]> {
    let params = new HttpParams()

    if (filters) {
      if (filters.amount !== undefined && filters.amount !== null) {
        params = params.set('amount', filters.amount.toString())
      }
      if (filters.operationDate) {
        params = params.set('operationDate', filters.operationDate)
      }
      if (filters.transactionDate) {
        params = params.set('transactionDate', filters.transactionDate)
      }
      if (filters.source) {
        params = params.set('source', filters.source)
      }
      if (filters.categoryId !== undefined && filters.categoryId !== null) {
        params = params.set('categoryId', filters.categoryId.toString())
      }
    }

    return this.http.get<Expense[]>(this.apiUrl, { params })
  }

  getById(id: number): Observable<Expense> {
    return this.http.get<Expense>(`${this.apiUrl}/${id}`)
  }

  create(payload: ExpensePayload): Observable<Expense> {
    return this.http.post<Expense>(this.apiUrl, payload)
  }

  update(id: number, payload: ExpensePayload): Observable<Expense> {
    return this.http.put<Expense>(`${this.apiUrl}/${id}`, payload)
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
  }
}
