package com.auth.example.config;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {

    @Bean
    public CommandLineRunner loadEnv() {
        return args -> {
            Dotenv dotenv = Dotenv.load();

            System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
            System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
            System.setProperty("DB_USERNAME", dotenv.get("MAIL_PASSWORD"));
            System.setProperty("DB_PASSWORD", dotenv.get("MAIL_PASSWORD"));

        };
    }
}
