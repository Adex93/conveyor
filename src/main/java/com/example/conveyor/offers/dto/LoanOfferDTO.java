package com.example.conveyor.offers.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class LoanOfferDTO {

    Long applicationId;
    BigDecimal requestedAmount;
    BigDecimal totalAmount;
    Integer term;
    BigDecimal monthlyPayment;
    BigDecimal rate;
    Boolean isInsuranceEnabled;
    Boolean isSalaryClient;


    public LoanOfferDTO(LoanApplicationRequestDTO loanApplicationRequestDTO) {

        this.applicationId = 0L;
        this.requestedAmount = loanApplicationRequestDTO.getAmount();
        this.term = loanApplicationRequestDTO.getTerm();
    }
}
