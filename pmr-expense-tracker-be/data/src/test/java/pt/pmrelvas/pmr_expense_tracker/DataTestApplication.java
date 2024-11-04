package pt.pmrelvas.pmr_expense_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"pt.pmrelvas.pmr_expese_tracker"})
@SpringBootApplication
public class DataTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataTestApplication.class, args);
    }
}
