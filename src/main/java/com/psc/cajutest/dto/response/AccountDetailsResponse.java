package com.psc.cajutest.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDetailsResponse {
    private String accountNumber;
    private Double foodBalance;
    private Double mealBalance;
    private Double cashBalance;
}
