package pt.pmrelvas.pmr_expense_tracker.entities.fake;

import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.entities.Expense;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public interface FakeExpenses {
    Category CAR_CATEGORY = Category.builder()
            .id(1L)
            .code("CAR")
            .name("Car")
            .createdAt(LocalDateTime.now(ZoneOffset.UTC))
            .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
            .build();

    Category FOOD_CATEGORY = Category.builder()
            .id(2L)
            .code("FOOD")
            .name("Food")
            .createdAt(LocalDateTime.now(ZoneOffset.UTC))
            .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
            .build();

    Expense GROCERY_EXPENSE = Expense.builder()
            .id(1L)
            .amount(new BigDecimal("50.25"))
            .totalBalance(new BigDecimal("1250.75"))
            .description("Grocery shopping")
            .operationDate(LocalDateTime.now(ZoneOffset.UTC))
            .transactionDate(LocalDateTime.now(ZoneOffset.UTC))
            .source("Credit Card")
            .category(FOOD_CATEGORY)
            .createdAt(LocalDateTime.now(ZoneOffset.UTC))
            .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
            .build();

    Expense FUEL_EXPENSE = Expense.builder()
            .id(2L)
            .amount(new BigDecimal("75.50"))
            .totalBalance(new BigDecimal("1175.25"))
            .description("Fuel refill")
            .operationDate(LocalDateTime.now(ZoneOffset.UTC))
            .transactionDate(LocalDateTime.now(ZoneOffset.UTC))
            .source("Debit Card")
            .category(CAR_CATEGORY)
            .createdAt(LocalDateTime.now(ZoneOffset.UTC))
            .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
            .build();

    Expense RESTAURANT_EXPENSE = Expense.builder()
            .id(3L)
            .amount(new BigDecimal("35.00"))
            .totalBalance(new BigDecimal("1140.25"))
            .description("Dinner at restaurant")
            .operationDate(LocalDateTime.now(ZoneOffset.UTC))
            .transactionDate(LocalDateTime.now(ZoneOffset.UTC))
            .source("Cash")
            .category(FOOD_CATEGORY)
            .createdAt(LocalDateTime.now(ZoneOffset.UTC))
            .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
            .build();

    List<Expense> ALL = List.of(GROCERY_EXPENSE, FUEL_EXPENSE, RESTAURANT_EXPENSE);
} 