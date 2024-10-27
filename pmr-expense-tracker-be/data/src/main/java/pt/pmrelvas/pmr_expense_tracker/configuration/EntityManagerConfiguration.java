package pt.pmrelvas.pmr_expense_tracker.configuration;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"pt.pmrelvas"})
public class EntityManagerConfiguration {
}
