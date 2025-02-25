package pt.pmrelvas.pmr_expense_tracker.controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import pt.pmrelvas.pmr_expense_tracker.configuration.RestExceptionHandler;

@SpringBootApplication
@Import(RestExceptionHandler.class)
public class ApiTest {
    public static void main(String[] args) {
        SpringApplication.run(ApiTest.class, args);
    }
}
