package pt.pmrelvas.pmr_expense_tracker.repositories.psql.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.pmrelvas.pmr_expense_tracker.entities.CategoryPsql;

import java.util.Optional;

public interface JpaCategoryRepository extends JpaRepository<CategoryPsql, Long> {

    Optional<CategoryPsql> findByCode(String code);

}
