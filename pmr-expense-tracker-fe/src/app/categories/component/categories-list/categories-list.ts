import { Component, computed, inject, OnInit } from '@angular/core';
import { CategoryListStore } from './store/categories-list.store';
import { TableModule } from 'primeng/table';
import { DatePipe } from '@angular/common';
import { ButtonDirective } from 'primeng/button';

@Component({
  selector: 'pet-categories-list',
  imports: [TableModule, DatePipe, ButtonDirective],
  templateUrl: './categories-list.html',
  providers: [CategoryListStore],
  styleUrl: './categories-list.scss',
})
export class CategoriesList implements OnInit {
  readonly store = inject(CategoryListStore);

  categories = computed(() => this.store.categories());

  ngOnInit(): void {
    this.store.fetchAll();
  }

  onCreateClick(): void {}
}
