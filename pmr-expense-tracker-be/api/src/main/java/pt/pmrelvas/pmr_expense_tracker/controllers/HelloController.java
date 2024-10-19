package pt.pmrelvas.pmr_expense_tracker.controllers;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/hello")
public class HelloController {

    @GetMapping
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello World", HttpStatusCode.valueOf(200));
    }
}
