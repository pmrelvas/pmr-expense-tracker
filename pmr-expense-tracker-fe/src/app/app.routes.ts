import { Routes } from '@angular/router';
import { CategoryListComponent } from './components/category-list/category-list.component';

export const routes: Routes = [
  {
    path: 'categories',
    component: CategoryListComponent
  },
  {
    path: '',
    redirectTo: '/categories',
    pathMatch: 'full'
  }
];
