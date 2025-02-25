package pt.pmrelvas.pmr_expense_tracker.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pt.pmrelvas.pmr_expense_tracker.payloads.request.CategoryFiltersApiRequestPayload;
import pt.pmrelvas.pmr_expense_tracker.payloads.response.CategoryApiRequestPayload;
import pt.pmrelvas.pmr_expense_tracker.payloads.response.CategoryApiResponsePayload;
import pt.pmrelvas.pmr_expense_tracker.usecases.categories.CreateCategoryUseCase;
import pt.pmrelvas.pmr_expense_tracker.usecases.categories.DeleteCategoryUseCase;
import pt.pmrelvas.pmr_expense_tracker.usecases.categories.ReadCategoryUseCase;
import pt.pmrelvas.pmr_expense_tracker.usecases.categories.UpdateCategoryUseCase;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ReadCategoryUseCase readCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final CreateCategoryUseCase createCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    @GetMapping
    public List<CategoryApiResponsePayload> fetchAll(@Valid CategoryFiltersApiRequestPayload filters) {
        return readCategoryUseCase.executeFindAll(filters.toEntity()).stream()
                .map(CategoryApiResponsePayload::new)
                .toList();
    }

    @GetMapping("{id}")
    public CategoryApiResponsePayload fetchById(@PathVariable long id) {
        return new CategoryApiResponsePayload(readCategoryUseCase.executeFindByIdOrThrow(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryApiResponsePayload create(@Valid @RequestBody CategoryApiRequestPayload payload) {
        return new CategoryApiResponsePayload(createCategoryUseCase.execute(payload.toEntity()));
    }

    @PutMapping("{id}")
    public CategoryApiResponsePayload update(
            @PathVariable long id,
            @Valid @RequestBody CategoryApiRequestPayload payload) {
        return new CategoryApiResponsePayload(updateCategoryUseCase.execute(id, payload.toEntity()));
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable long id) {
        deleteCategoryUseCase.execute(id);
    }
}
