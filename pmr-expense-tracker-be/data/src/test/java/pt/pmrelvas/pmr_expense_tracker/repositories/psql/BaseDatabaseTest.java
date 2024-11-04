package pt.pmrelvas.pmr_expense_tracker.repositories.psql;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pt.pmrelvas.pmr_expense_tracker.repositories.CategoryRepository;

import java.util.Stack;

@DataJpaTest
public abstract class BaseDatabaseTest {

    private final Stack<Long> createdCategoryIds = new Stack<>();

    @Autowired
    protected CategoryRepository categoryRepository;

    @AfterEach
    void tearDown() {
        while (createdCategoryIds.peek() != null) {
            categoryRepository.delete(createdCategoryIds.pop());
        }
    }
}
