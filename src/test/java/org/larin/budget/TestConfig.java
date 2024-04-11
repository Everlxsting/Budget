package org.larin.budget;


import org.larin.budget.data.entity.Person;
import org.larin.budget.data.repository.PersonRepository;
import org.larin.budget.service.Principal;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    public UserDetailsService userDetailsService(PersonRepository repo) {
        return s -> {
            Person p = repo.findByUsername(s).orElseGet(() -> {
                Person fake = new Person();
                fake.setUsername(s);
                return fake;
            });
            return new Principal(p);
        };
    }
}
