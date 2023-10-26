package com.example.springlogin;

import com.example.springlogin.config.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
@Import(ApplicationConfig.class)
public class SpringLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringLoginApplication.class, args);
    }

}
