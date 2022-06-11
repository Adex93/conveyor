package com.example.conveyor.calculation.service;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class CalculatorCredit {


    public static BigDecimal calculatePSK(BigDecimal monthlyAmount, BigDecimal amount, Integer term) {

        BigDecimal result;
        result = ((monthlyAmount.multiply(BigDecimal.valueOf(term))).divide(amount, 50, RoundingMode.HALF_UP).subtract(BigDecimal.ONE)).
                divide(BigDecimal.valueOf(term).divide(BigDecimal.valueOf(12),50, RoundingMode.HALF_UP), 50, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        return result.setScale(3, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateRatePayment(BigDecimal rate, BigDecimal amount) {
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(1200), 30, RoundingMode.HALF_UP);

        return amount.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
    }


    public static BigDecimal calculateBodyPayment(BigDecimal RatePayment, BigDecimal monthlyPayment) {

        return monthlyPayment.subtract(RatePayment).setScale(2, RoundingMode.HALF_UP);
    }


}

