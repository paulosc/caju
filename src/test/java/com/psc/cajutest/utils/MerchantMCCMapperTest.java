package com.psc.cajutest.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MerchantMCCMapperTest {

    @Test
    public void testGetCategory_MerchantMatch() {
        // Test cases where merchant name should determine the category
        assertEquals(BalanceType.FOOD, MerchantMCCMapper.getCategory("UBER EATS", "5411"));
        assertEquals(BalanceType.CASH, MerchantMCCMapper.getCategory("UBER TRIP", "5811"));
        assertEquals(BalanceType.MEAL, MerchantMCCMapper.getCategory("PAG*JoseDaSilva", "5411"));
        assertEquals(BalanceType.MEAL, MerchantMCCMapper.getCategory("PICPAY*BILHETEUNICO", "5811"));
    }

    @Test
    public void testGetCategory_MccMatch() {
        // Test cases where merchant name does not match but MCC should determine the category
        assertEquals(BalanceType.FOOD, MerchantMCCMapper.getCategory("", "5411"));
        assertEquals(BalanceType.MEAL, MerchantMCCMapper.getCategory("", "5811"));
        assertEquals(BalanceType.CASH, MerchantMCCMapper.getCategory("", "1234"));
    }

    @Test
    public void testGetCategory_NoMatch() {
        // Test case where neither merchant name nor MCC match
        assertEquals(BalanceType.CASH, MerchantMCCMapper.getCategory("UNKNOWN MERCHANT", "9999"));
    }
}
