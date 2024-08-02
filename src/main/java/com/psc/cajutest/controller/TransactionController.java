package com.psc.cajutest.controller;

import com.psc.cajutest.dto.request.TransactionRequest;
import com.psc.cajutest.dto.response.TransactionResponse;
import com.psc.cajutest.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> authorizeTransaction(@RequestBody TransactionRequest transactionRequest) {
        return new ResponseEntity<>(transactionService.processTransaction(transactionRequest), HttpStatus.OK);
    }
}
