package com.psc.cajutest;

import com.psc.cajutest.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CajuTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CajuTestApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(AccountService accountService) {
        return args -> {
            // Lista de números de conta para criar
            String[] accountNumbers = {"123", "456", "789"};

            for (String accountNumber : accountNumbers) {
                // Verifica se a conta já existe
                if (accountService.getAccount(accountNumber) == null) {
                    // Cria a conta se não existir
                    accountService.createAccount(accountNumber);
                }
            }
        };
    }
}
