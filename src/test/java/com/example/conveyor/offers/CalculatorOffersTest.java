package com.example.conveyor.offers;

import com.example.conveyor.offers.service.CalculatorOffers;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorOffersTest {


    @Test
    void calculateMonthlyPaymentShouldReturnBigDecimal() {

        BigDecimal expected = BigDecimal.valueOf(101791.61);
        BigDecimal amount = BigDecimal.valueOf(2000000);
        Integer term = 24;
        BigDecimal rate = BigDecimal.valueOf(20);
        BigDecimal actual = CalculatorOffers.calculateMonthlyPayment(amount, term, rate);
        assertEquals(expected, actual);

    }
}