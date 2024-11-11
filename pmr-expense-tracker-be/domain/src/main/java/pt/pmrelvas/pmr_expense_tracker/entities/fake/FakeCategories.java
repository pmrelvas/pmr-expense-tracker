package pt.pmrelvas.pmr_expense_tracker.entities.fake;

import pt.pmrelvas.pmr_expense_tracker.entities.Category;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public interface FakeCategories {
    Category CAR = Category.builder()
            .code("CAR")
            .name("Car")
            .createdAt(LocalDateTime.now(ZoneOffset.UTC))
            .build();

    Category HOUSE = Category.builder()
            .code("HOUSE")
            .name("House")
            .createdAt(LocalDateTime.now(ZoneOffset.UTC))
            .build();

    Category FUEL = Category.builder()
            .code("FUEL")
            .name("Fuel")
            .parentCategory(CAR)
            .createdAt(LocalDateTime.now(ZoneOffset.UTC))
            .build();

    List<Category> ALL = List.of(CAR, HOUSE, FUEL);
}
