package com.psc.cajutest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private String account;
    private double totalAmount;
    private String merchant;
    private String mcc;
}

