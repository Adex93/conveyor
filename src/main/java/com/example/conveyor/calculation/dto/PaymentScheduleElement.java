package com.example.conveyor.calculation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class PaymentScheduleElement {
    Integer number;
    LocalDate date;
    BigDecimal totalPayment;
    BigDecimal interestPayment;
    BigDecimal debtPayment;
    BigDecimal remainingDebt;
}
