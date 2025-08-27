import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category, CategoryFilter } from '../model/categories.model';
import { buildHttpParams } from '../../shared/utils/api-utils';

@Injectable({ providedIn: 'root' })
export class CategoryApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/v1/categories';

  fetchAll(params?: CategoryFilter): Observable<Category[]> {
    const httpParams = buildHttpParams({
      code: params?.code,
      name: params?.name,
      'parent-category-id': params?.parentCategoryId,
    });
    return this.http.get<Category[]>(this.baseUrl, { params: httpParams });
  }
}
