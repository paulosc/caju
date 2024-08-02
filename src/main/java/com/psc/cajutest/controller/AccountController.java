package com.psc.cajutest.controller;

import com.psc.cajutest.dto.response.AccountDetailsResponse;
import com.psc.cajutest.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public List<AccountDetailsResponse> getAllAccounts() {
        return accountService.getAllAccounts().stream()
                .map(account -> AccountDetailsResponse.builder()
                        .accountNumber(account.getAccountNumber())
                        .foodBalance(account.getFoodBalance())
                        .mealBalance(account.getMealBalance())
                        .cashBalance(account.getCashBalance())
                        .build())
                .collect(Collectors.toList());
    }
}
