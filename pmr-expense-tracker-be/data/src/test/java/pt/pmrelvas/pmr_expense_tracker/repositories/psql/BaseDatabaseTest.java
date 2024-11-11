package pt.pmrelvas.pmr_expense_tracker.repositories.psql;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.repositories.CategoryRepository;

import java.util.Stack;

@Testcontainers
@SpringBootTest
public abstract class BaseDatabaseTest {

    private final Stack<Long> createdCategoryIds = new Stack<>();

    @Autowired
    protected CategoryRepository categoryRepository;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("expense_tracker")
            .withUsername("postgres")
            .withPassword("postgres");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @AfterEach
    void tearDown() {
        for (Long createdCategoryId : createdCategoryIds) {
            categoryRepository.deleteById(createdCategoryId);
        }
        createdCategoryIds.clear();
    }

    public Category create(Category category) {
        Category insertedCategory = categoryRepository.upsert(category);
        createdCategoryIds.push(insertedCategory.id());
        return insertedCategory;
    }
}
