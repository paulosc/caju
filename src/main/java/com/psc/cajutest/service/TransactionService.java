package com.psc.cajutest.service;

import com.psc.cajutest.dto.request.TransactionRequest;
import com.psc.cajutest.dto.response.ResponseCode;
import com.psc.cajutest.dto.response.TransactionResponse;
import com.psc.cajutest.model.Account;
import com.psc.cajutest.model.Transaction;
import com.psc.cajutest.repository.AccountRepository;
import com.psc.cajutest.repository.TransactionRepository;
import com.psc.cajutest.utils.BalanceType;
import com.psc.cajutest.utils.MerchantMCCMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public TransactionResponse processTransaction(TransactionRequest transactionRequest) {
        try {
            Transaction transaction = modelMapper.map(transactionRequest, Transaction.class);
            Account account = accountRepository.findByAccountNumber(transactionRequest.getAccount());

            if (account == null) {
                return createErrorResponse();
            }

            BalanceType balanceType = MerchantMCCMapper.getCategory(transaction.getMerchant(), transaction.getMcc());

            if (processTransactionForCategory(account, balanceType, transaction.getTotalAmount())) {
                return saveTransactionAndRespond(transaction, ResponseCode.APPROVED);
            }

            if (balanceType == BalanceType.FOOD || balanceType == BalanceType.MEAL) {
                if (processTransactionForCategory(account, BalanceType.CASH, transaction.getTotalAmount())) {
                    return saveTransactionAndRespond(transaction, ResponseCode.APPROVED);
                }
            }

            return createResponse(ResponseCode.INSUFFICIENT_FUNDS);
        } catch (Exception e) {
            log.error("Error processing transaction", e);
            return createErrorResponse();
        }
    }

    private boolean processTransactionForCategory(Account account, BalanceType balanceType, Double amount) {
        Double currentBalance = getBalance(account, balanceType);
        if (currentBalance >= amount) {
            updateBalance(account, balanceType, currentBalance - amount);
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    private Double getBalance(Account account, BalanceType balanceType) {
        switch (balanceType) {
            case FOOD:
                return account.getFoodBalance();
            case MEAL:
                return account.getMealBalance();
            case CASH:
                return account.getCashBalance();
            default:
                return 0.0;
        }
    }

    private void updateBalance(Account account, BalanceType balanceType, Double newBalance) {
        switch (balanceType) {
            case FOOD:
                account.setFoodBalance(newBalance);
                break;
            case MEAL:
                account.setMealBalance(newBalance);
                break;
            case CASH:
                account.setCashBalance(newBalance);
                break;
        }
    }

    private TransactionResponse saveTransactionAndRespond(Transaction transaction, ResponseCode responseCode) {
        transaction.setCode(responseCode.getCode());
        transaction.setDate(LocalDateTime.now());
        transactionRepository.save(transaction);
        return createResponse(responseCode);
    }

    private TransactionResponse createErrorResponse() {
        return createResponse(ResponseCode.ERROR);
    }

    private TransactionResponse createResponse(ResponseCode responseCode) {
        return TransactionResponse.builder()
                .code(responseCode.getCode())
                .build();
    }
}
