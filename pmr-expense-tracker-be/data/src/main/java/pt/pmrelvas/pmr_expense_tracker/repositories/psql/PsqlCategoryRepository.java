package pt.pmrelvas.pmr_expense_tracker.repositories.psql;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.entities.CategoryPsql;
import pt.pmrelvas.pmr_expense_tracker.entities.QCategoryPsql;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.CategoryFilters;
import pt.pmrelvas.pmr_expense_tracker.repositories.CategoryRepository;
import pt.pmrelvas.pmr_expense_tracker.repositories.psql.jpa.JpaCategoryRepository;

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
        QCategoryPsql qCategoryPsql = QCategoryPsql.categoryPsql;

        JPAQuery<CategoryPsql> query = new JPAQueryFactory(entityManager).selectFrom(qCategoryPsql);

        buildWhereClauses(filters, query, qCategoryPsql);
        return query.distinct().fetch().stream()
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

    private static void buildWhereClauses(CategoryFilters filters, JPAQuery<CategoryPsql> query, QCategoryPsql qCategoryPsql) {
        if (filters.code() != null) {
            query.where(qCategoryPsql.code.containsIgnoreCase(filters.code()));
        }

        if (filters.name() != null) {
            query.where(qCategoryPsql.name.containsIgnoreCase(filters.name()));
        }

        if (filters.parentCategoryId() != null) {
            query.where(qCategoryPsql.parentCategory.id.eq(filters.parentCategoryId()));
        }
    }

}
