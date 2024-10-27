package pt.pmrelvas.pmr_expense_tracker.repositories.psql;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.entities.CategoryPsql;
import pt.pmrelvas.pmr_expense_tracker.entities.QCategoryPsql;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.CategoryFilters;
import pt.pmrelvas.pmr_expense_tracker.repositories.CategoryRepository;
import pt.pmrelvas.pmr_expense_tracker.repositories.psql.jpa.JpaCategoryRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PsqlCategoryRepository implements CategoryRepository {

    private final JpaCategoryRepository jpaCategoryRepository;
    private final EntityManager entityManager;

    @Override
    public List<Category> findAll(CategoryFilters filters) {
        QCategoryPsql qCategoryPsql = QCategoryPsql.categoryPsql;

        JPAQuery<CategoryPsql> query = new JPAQueryFactory(entityManager).selectFrom(qCategoryPsql);

        buildWhereClauses(filters, query, qCategoryPsql);
        return query.distinct().fetch().stream()
                .map(CategoryPsql::toEntity)
                .toList();
    }

    private static void buildWhereClauses(CategoryFilters filters, JPAQuery<CategoryPsql> query, QCategoryPsql qCategoryPsql) {
        if (filters.code() != null) {
            query.where(qCategoryPsql.code.contains(filters.code()));
        }

        if (filters.name() != null) {
            query.where(qCategoryPsql.name.contains(filters.name()));
        }

        if (filters.parentCategoryId() != null) {
            query.where(qCategoryPsql.parentCategory.id.eq(filters.parentCategoryId()));
        }
    }

    @Override
    public Category upsert(Category category) {
        return jpaCategoryRepository.save(new CategoryPsql(category)).toEntity();
    }

    @Override
    public void delete(long id) {
        jpaCategoryRepository.deleteById(id);
    }
}
