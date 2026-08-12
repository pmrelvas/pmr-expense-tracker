import { Injectable, inject } from '@angular/core'
import { HttpClient, HttpParams } from '@angular/common/http'
import { Observable } from 'rxjs'
import { Category, CategoryFilters } from '../models/category.model'

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private readonly http = inject(HttpClient)
  private readonly apiUrl = 'http://localhost:8080/api/v1/categories'

  getAll(filters?: CategoryFilters): Observable<Category[]> {
    let params = new HttpParams()
    
    if (filters) {
      if (filters.code) {
        params = params.set('code', filters.code)
      }
      if (filters.name) {
        params = params.set('name', filters.name)
      }
      if (filters['parent-category-id']) {
        params = params.set('parent-category-id', filters['parent-category-id'].toString())
      }
    }

    return this.http.get<Category[]>(this.apiUrl, { params })
  }

  getById(id: number): Observable<Category> {
    return this.http.get<Category>(`${this.apiUrl}/${id}`)
  }
}

