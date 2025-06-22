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
import pt.pmrelvas.pmr_expense_tracker.payloads.request.ExpenseFiltersApiRequestPayload;
import pt.pmrelvas.pmr_expense_tracker.payloads.request.ExpenseApiRequestPayload;
import pt.pmrelvas.pmr_expense_tracker.payloads.response.ExpenseApiResponsePayload;
import pt.pmrelvas.pmr_expense_tracker.usecases.expenses.CreateExpenseUseCase;
import pt.pmrelvas.pmr_expense_tracker.usecases.expenses.DeleteExpenseUseCase;
import pt.pmrelvas.pmr_expense_tracker.usecases.expenses.ReadExpenseUseCase;
import pt.pmrelvas.pmr_expense_tracker.usecases.expenses.UpdateExpenseUseCase;

import java.util.List;

@RestController
@RequestMapping("api/v1/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ReadExpenseUseCase readExpenseUseCase;
    private final UpdateExpenseUseCase updateExpenseUseCase;
    private final CreateExpenseUseCase createExpenseUseCase;
    private final DeleteExpenseUseCase deleteExpenseUseCase;

    @GetMapping
    public List<ExpenseApiResponsePayload> fetchAll(@Valid ExpenseFiltersApiRequestPayload filters) {
        return readExpenseUseCase.executeFindAll(filters.toEntity(), null, null).stream()
                .map(ExpenseApiResponsePayload::new)
                .toList();
    }

    @GetMapping("{id}")
    public ExpenseApiResponsePayload fetchById(@PathVariable long id) {
        return new ExpenseApiResponsePayload(readExpenseUseCase.executeFindByIdOrThrow(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseApiResponsePayload create(@Valid @RequestBody ExpenseApiRequestPayload payload) {
        return new ExpenseApiResponsePayload(createExpenseUseCase.execute(payload.toEntity()));
    }

    @PutMapping("{id}")
    public ExpenseApiResponsePayload update(
            @PathVariable long id,
            @Valid @RequestBody ExpenseApiRequestPayload payload) {
        return new ExpenseApiResponsePayload(updateExpenseUseCase.execute(id, payload.toEntity()));
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable long id) {
        deleteExpenseUseCase.execute(id);
    }
} 