import { signalStore, withState, withMethods, patchState } from '@ngrx/signals';
import { Category, CategoryFilter } from '../../../model/categories.model';
import { pipe, switchMap, tap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { inject } from '@angular/core';
import { CategoryApiService } from '../../../service/category-api';

type CategoryListState = {
  categories: Category[];
  isLoading: boolean;
  filters: CategoryFilter;
};

const initialState: CategoryListState = {
  categories: [],
  isLoading: false,
  filters: {},
};

export const CategoryListStore = signalStore(
  withState(initialState),
  withMethods((store, categoryApiService = inject(CategoryApiService)) => ({
    fetchAll: rxMethod<void>(
      pipe(
        tap(() => patchState(store, { isLoading: true })),
        switchMap(() => {
          return categoryApiService.fetchAll(store.filters()).pipe(
            tapResponse({
              next: (categories) => patchState(store, { categories }),
              error: (_) => console.log('Error loading categories'),
              finalize: () => patchState(store, { isLoading: false }),
            })
          );
        })
      )
    ),
  }))
);
