package com.psc.cajutest.utils;

public class MerchantMCCMapper {

    public static BalanceType getCategory(String merchant, String mcc) {
        // Check merchant name first
        if (merchant.contains("UBER EATS")) {
            return BalanceType.FOOD;
        } else if (merchant.contains("UBER TRIP")) {
            return BalanceType.CASH;
        } else if (merchant.contains("PAG*JoseDaSilva")) {
            return BalanceType.MEAL;
        } else if (merchant.contains("PICPAY*BILHETEUNICO")) {
            return BalanceType.MEAL;
        }

        // Default to MCC if no merchant match found
        switch (mcc) {
            case "5411":
            case "5412":
                return BalanceType.FOOD;
            case "5811":
            case "5812":
                return BalanceType.MEAL;
            default:
                return BalanceType.CASH;
        }
    }
}
