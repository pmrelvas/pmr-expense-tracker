package pt.pmrelvas.pmr_expense_tracker.entities.fake;

import pt.pmrelvas.pmr_expense_tracker.entities.Category;

import java.util.List;

public interface FakeCategories {
    Category CAR = Category.builder()
            .code("CAR")
            .name("Car")
            .build();

    Category HOUSE = Category.builder()
            .code("HOUSE")
            .name("House")
            .build();

    Category FUEL = Category.builder()
            .code("FUEL")
            .name("Fuel")
            .parentCategory(CAR)
            .build();

    List<Category> ALL = List.of(CAR, HOUSE, FUEL);
}
