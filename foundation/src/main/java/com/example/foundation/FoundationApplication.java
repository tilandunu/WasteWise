package com.example.foundation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class FoundationApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(FoundationApplication.class, args);
        Environment env = context.getEnvironment();

        String port = env.getProperty("server.port", "8080");
        System.out.println("Application is running on http://localhost:" + port);
    }
}
