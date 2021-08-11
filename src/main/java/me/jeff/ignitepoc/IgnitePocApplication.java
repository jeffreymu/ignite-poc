package me.jeff.ignitepoc;

import org.apache.ignite.springdata22.repository.config.EnableIgniteRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableIgniteRepositories
public class IgnitePocApplication {

    public static void main(String[] args) {
        SpringApplication.run(IgnitePocApplication.class, args);
    }

}
