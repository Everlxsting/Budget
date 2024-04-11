package org.larin.budget;

import org.larin.budget.service.PrincipalDetailsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootApplication
public class BudgetApplication {

    @Bean
    public UserDetailsService userDetailsService() {
        return new PrincipalDetailsService();
    }

    public static void main(String[] args) {
        SpringApplication.run(BudgetApplication.class, args);
    }
}
