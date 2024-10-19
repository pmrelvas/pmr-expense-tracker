package pt.pmrelvas.pmr_expense_tracker.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.pmrelvas.pmr_expense_tracker.usecases.SayHelloUseCase;

@RestController
@RequestMapping("api/v1/hello")
@RequiredArgsConstructor
public class HelloController {

    private final SayHelloUseCase sayHelloUseCase;

    @GetMapping
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>(sayHelloUseCase.execute(), HttpStatusCode.valueOf(200));
    }
}
