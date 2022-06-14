package com.example.conveyor.offers.service;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Slf4j
public class CalculatorOffers {
    public static BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate) {

        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(1200), 50, RoundingMode.HALF_UP);
        BigDecimal result = amount.multiply(monthlyRate.divide(BigDecimal.ONE.subtract((BigDecimal.ONE.add(monthlyRate)).pow(-term, new MathContext(50))), 50, RoundingMode.HALF_UP));

        return result.setScale(2, RoundingMode.HALF_UP);

    }
}

