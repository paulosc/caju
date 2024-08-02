package com.psc.cajutest.service;

import com.psc.cajutest.dto.request.TransactionRequest;
import com.psc.cajutest.dto.response.ResponseCode;
import com.psc.cajutest.dto.response.TransactionResponse;
import com.psc.cajutest.model.Account;
import com.psc.cajutest.model.Transaction;
import com.psc.cajutest.repository.AccountRepository;
import com.psc.cajutest.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessTransaction_Approved() {
        // Arrange
        TransactionRequest request = new TransactionRequest("123", 50.0, "Test Merchant", "5811");
        Account account = new Account();
        account.setAccountNumber("123");
        account.setCashBalance(1000.0);
        account.setFoodBalance(1000.0);
        account.setMealBalance(1000.0);
        Transaction transaction = new Transaction();

        when(accountRepository.findByAccountNumber("123")).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        TransactionResponse response = transactionService.processTransaction(request);

        // Assert
        assertEquals(ResponseCode.APPROVED.getCode(), response.getCode());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void testProcessTransaction_InsufficientFunds() {
        // Arrange
        TransactionRequest request = new TransactionRequest("123", 2000.0, "Test Merchant", "5811");
        Account account = new Account();
        account.setAccountNumber("123");
        account.setCashBalance(1000.0);
        account.setFoodBalance(1000.0);
        account.setMealBalance(1000.0);
        Transaction transaction = new Transaction();

        when(accountRepository.findByAccountNumber("123")).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        TransactionResponse response = transactionService.processTransaction(request);

        // Assert
        assertEquals(ResponseCode.INSUFFICIENT_FUNDS.getCode(), response.getCode());
    }

    @Test
    void testProcessTransaction_Error() {
        // Arrange
        TransactionRequest request = new TransactionRequest("123", 50.0, "Test Merchant", "5811");
        when(accountRepository.findByAccountNumber("123")).thenThrow(new RuntimeException("Database error"));

        // Act
        TransactionResponse response = transactionService.processTransaction(request);

        // Assert
        assertEquals(ResponseCode.ERROR.getCode(), response.getCode());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}
