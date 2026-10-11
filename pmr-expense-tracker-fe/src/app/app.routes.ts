import { Routes } from '@angular/router';
import { CategoryListComponent } from './components/category-list/category-list.component';
import { ExpenseListComponent } from './components/expense-list/expense-list.component';

export const routes: Routes = [
  {
    path: 'categories',
    component: CategoryListComponent
  },
  {
    path: 'expenses',
    component: ExpenseListComponent
  },
  {
    path: '',
    redirectTo: '/categories',
    pathMatch: 'full'
  }
];
