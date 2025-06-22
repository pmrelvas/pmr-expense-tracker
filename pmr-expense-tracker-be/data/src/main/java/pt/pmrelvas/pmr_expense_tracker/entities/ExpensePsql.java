package pt.pmrelvas.pmr_expense_tracker.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import com.querydsl.core.types.dsl.ComparableExpressionBase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.pmrelvas.pmr_expense_tracker.entities.Expense.SortingField;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
public class ExpensePsql {


    @Id
    @SequenceGenerator(name = "expenses_seq", sequenceName = "expenses_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "expenses_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "total_balance")
    private BigDecimal totalBalance;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "operation_date", nullable = false)
    private LocalDateTime operationDate;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "source")
    private String source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_category")
    private CategoryPsql category;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ExpensePsql(Expense expense) {
        this.id = expense.id();
        this.amount = expense.amount();
        this.totalBalance = expense.totalBalance();
        this.description = expense.description();
        this.operationDate = expense.operationDate();
        this.transactionDate = expense.transactionDate();
        this.source = expense.source();
        this.category = expense.category() == null ? null : new CategoryPsql(expense.category());
        this.createdAt = expense.createdAt();
        this.updatedAt = expense.updatedAt();
    }

    public Expense toEntity() {
        return Expense.builder()
                .id(this.id)
                .amount(this.amount)
                .totalBalance(this.totalBalance)
                .description(this.description)
                .operationDate(this.operationDate)
                .transactionDate(this.transactionDate)
                .source(this.source)
                .category(this.category == null ? null : this.category.toEntity())
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
