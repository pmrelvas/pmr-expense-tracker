package pt.pmrelvas.pmr_expense_tracker.repositories.psql.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.pmrelvas.pmr_expense_tracker.entities.CategoryPsql;

public interface JpaCategoryRepository extends JpaRepository<CategoryPsql, Long> {
}
