package pt.pmrelvas.pmr_expense_tracker.repositories.psql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.entities.CategoryPsql;
import pt.pmrelvas.pmr_expense_tracker.entities.CategoryPsql_;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.CategoryFilters;
import pt.pmrelvas.pmr_expense_tracker.repositories.CategoryRepository;
import pt.pmrelvas.pmr_expense_tracker.repositories.psql.jpa.JpaCategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PsqlCategoryRepository implements CategoryRepository {

    private final JpaCategoryRepository jpaCategoryRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public List<Category> findAll(CategoryFilters filters) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CategoryPsql> query = cb.createQuery(CategoryPsql.class);
        Root<CategoryPsql> root = query.from(CategoryPsql.class);

        // Apply filters
        List<Predicate> predicates = buildPredicates(filters, cb, root);
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        // Execute query
        List<CategoryPsql> results = entityManager.createQuery(query).getResultList();

        return results.stream()
                .map(CategoryPsql::toEntity)
                .toList();
    }

    @Override
    public Optional<Category> findById(long id) {
        return jpaCategoryRepository.findById(id).map(CategoryPsql::toEntity);
    }

    @Transactional
    @Override
    public Category upsert(Category category) {
        CategoryPsql categoryPsql = new CategoryPsql(category);
        if (category.parentCategory() != null) {
            categoryPsql.setParentCategory(jpaCategoryRepository.findByCode(category.parentCategory().code()).orElse(null));
        }
        return jpaCategoryRepository.save(categoryPsql).toEntity();
    }

    @Override
    public void deleteById(long id) {
        jpaCategoryRepository.deleteById(id);
    }

    private List<Predicate> buildPredicates(CategoryFilters filters, CriteriaBuilder cb, Root<CategoryPsql> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (filters.code() != null) {
            predicates.add(cb.like(cb.lower(root.get(CategoryPsql_.code)), "%" + filters.code().toLowerCase() + "%"));
        }

        if (filters.name() != null) {
            predicates.add(cb.like(cb.lower(root.get(CategoryPsql_.name)), "%" + filters.name().toLowerCase() + "%"));
        }

        if (filters.parentCategoryId() != null) {
            predicates.add(cb.equal(root.get(CategoryPsql_.parentCategory).get(CategoryPsql_.id), filters.parentCategoryId()));
        }

        return predicates;
    }
}
