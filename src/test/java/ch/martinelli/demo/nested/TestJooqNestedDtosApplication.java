package ch.martinelli.demo.nested;

import org.springframework.boot.SpringApplication;

public class TestJooqNestedDtosApplication {

    public static void main(String[] args) {
        SpringApplication.from(JooqNestedDtosApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
