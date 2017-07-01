package com.example.booklist.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by ka.wada on 2017/07/01.
 */
@SpringBootApplication(scanBasePackages = "com.example.booklist")
@EnableJpaRepositories(basePackages = {"com.example.booklist.shared"})
@EntityScan("com.example.booklist.shared")
public class BooklistApplication {
    public static void main(String[] args) {
        SpringApplication.run(BooklistApplication.class, args);
    }
}
