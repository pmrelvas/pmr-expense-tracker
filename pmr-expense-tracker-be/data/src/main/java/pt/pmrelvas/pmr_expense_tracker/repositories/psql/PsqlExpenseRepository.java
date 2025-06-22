package pt.pmrelvas.pmr_expense_tracker.repositories.psql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.pmrelvas.pmr_expense_tracker.entities.CategoryPsql_;
import pt.pmrelvas.pmr_expense_tracker.entities.Expense;
import pt.pmrelvas.pmr_expense_tracker.entities.Expense.SortingField;
import pt.pmrelvas.pmr_expense_tracker.entities.ExpensePsql;
import pt.pmrelvas.pmr_expense_tracker.entities.ExpensePsql_;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.ExpenseFilters;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.PageFilter;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.SortDirection;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.SortFilter;
import pt.pmrelvas.pmr_expense_tracker.repositories.ExpenseRepository;
import pt.pmrelvas.pmr_expense_tracker.repositories.psql.jpa.JpaExpenseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PsqlExpenseRepository implements ExpenseRepository {

    private final JpaExpenseRepository jpaExpenseRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public List<Expense> findAll(ExpenseFilters filters, SortFilter<SortingField> sortFilter, PageFilter pageFilter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ExpensePsql> query = cb.createQuery(ExpensePsql.class);
        Root<ExpensePsql> root = query.from(ExpensePsql.class);

        // Apply filters
        List<Predicate> predicates = buildPredicates(filters, cb, root);
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        // Apply sorting
        if (sortFilter != null) {
            applySorting(sortFilter, cb, query, root);
        }

        // Execute query
        List<ExpensePsql> results = entityManager.createQuery(query).getResultList();

        // Apply pagination
        if (pageFilter != null) {
            int start = pageFilter.index() * pageFilter.size();
            int end = Math.min(start + pageFilter.size(), results.size());
            results = results.subList(start, end);
        }

        return results.stream()
                .map(ExpensePsql::toEntity)
                .toList();
    }

    @Override
    public Optional<Expense> findById(long id) {
        return jpaExpenseRepository.findById(id).map(ExpensePsql::toEntity);
    }

    @Transactional
    @Override
    public Expense upsert(Expense expense) {
        ExpensePsql expensePsql = new ExpensePsql(expense);
        if (expense.category() != null) {
            // Note: You might need to inject JpaCategoryRepository here if you need to fetch the category
            // For now, we'll assume the category is already properly set in the ExpensePsql constructor
        }
        return jpaExpenseRepository.save(expensePsql).toEntity();
    }

    @Override
    public void deleteById(long id) {
        jpaExpenseRepository.deleteById(id);
    }

    private List<Predicate> buildPredicates(ExpenseFilters filters, CriteriaBuilder cb, Root<ExpensePsql> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (filters.amount() != null) {
            predicates.add(cb.equal(root.get(ExpensePsql_.amount), filters.amount()));
        }

        if (filters.operationDate() != null) {
            predicates.add(cb.equal(root.get(ExpensePsql_.operationDate), filters.operationDate()));
        }

        if (filters.transactionDate() != null) {
            predicates.add(cb.equal(root.get(ExpensePsql_.transactionDate), filters.transactionDate()));
        }

        if (filters.source() != null) {
            predicates.add(cb.like(cb.lower(root.get(ExpensePsql_.source)), "%" + filters.source().toLowerCase() + "%"));
        }

        if (filters.categoryId() != null) {
            predicates.add(cb.equal(root.get(ExpensePsql_.category).get(CategoryPsql_.id), filters.categoryId()));
        }

        return predicates;
    }

    private void applySorting(SortFilter<SortingField> sortFilter, CriteriaBuilder cb, CriteriaQuery<ExpensePsql> query, Root<ExpensePsql> root) {
        switch (sortFilter.field()) {
            case AMOUNT:
                if (sortFilter.direction() == SortDirection.ASC) {
                    query.orderBy(cb.asc(root.get(ExpensePsql_.amount)));
                } else {
                    query.orderBy(cb.desc(root.get(ExpensePsql_.amount)));
                }
                break;
            case OPERATION_DATE:
                if (sortFilter.direction() == SortDirection.ASC) {
                    query.orderBy(cb.asc(root.get(ExpensePsql_.operationDate)));
                } else {
                    query.orderBy(cb.desc(root.get(ExpensePsql_.operationDate)));
                }
                break;
            case TRANSACTION_DATE:
                if (sortFilter.direction() == SortDirection.ASC) {
                    query.orderBy(cb.asc(root.get(ExpensePsql_.transactionDate)));
                } else {
                    query.orderBy(cb.desc(root.get(ExpensePsql_.transactionDate)));
                }
                break;
            case SOURCE:
                if (sortFilter.direction() == SortDirection.ASC) {
                    query.orderBy(cb.asc(root.get(ExpensePsql_.source)));
                } else {
                    query.orderBy(cb.desc(root.get(ExpensePsql_.source)));
                }
                break;
            case CATEGORY:
                if (sortFilter.direction() == SortDirection.ASC) {
                    query.orderBy(cb.asc(root.get(ExpensePsql_.category).get(CategoryPsql_.name)));
                } else {
                    query.orderBy(cb.desc(root.get(ExpensePsql_.category).get(CategoryPsql_.name)));
                }
                break;
        }
    }
}
