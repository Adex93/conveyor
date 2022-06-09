package com.example.conveyor.calculation;

import com.example.conveyor.calculation.service.CalculatorCredit;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorCreditTest {

    @Test
    void calculatePSKShouldReturnBigDecimal() {
        BigDecimal expected = BigDecimal.valueOf(10.518);
        BigDecimal amount = BigDecimal.valueOf(2000000);
        Integer term = 24;
        BigDecimal monthlyAmount = BigDecimal.valueOf(100864.01);
        BigDecimal actual = CalculatorCredit.calculatePSK(monthlyAmount, amount, term);
        assertEquals(expected, actual);
    }

    @Test
    void calculateRatePaymentShouldReturnBigDecimal() {
        BigDecimal amount = BigDecimal.valueOf(2060000);
        BigDecimal rate = BigDecimal.valueOf(16);
        BigDecimal expected = BigDecimal.valueOf(27466.67);
        BigDecimal actual = CalculatorCredit.calculateRatePayment(rate, amount);
        assertEquals(expected, actual);
    }

    @Test
    void calculateBodyPaymentShouldReturnBigDecimal() {
        BigDecimal ratePayment = BigDecimal.valueOf(27466.67);
        BigDecimal monthlyPayment = BigDecimal.valueOf(100864.01);
        BigDecimal expected = BigDecimal.valueOf(73397.34);
        BigDecimal actual = CalculatorCredit.calculateBodyPayment(ratePayment, monthlyPayment);
        assertEquals(expected, actual);

    }
}