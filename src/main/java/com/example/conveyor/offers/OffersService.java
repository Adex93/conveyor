package com.example.conveyor.offers;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Hidden
@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
@Component
@Scope("prototype")
public class OffersService {

    public static Long idLoadOfferDTO = 0L;
    List<LoanOfferDTO> list = new ArrayList<>();
    @Value("${custom.calculating.baseRate}")
    BigDecimal rate;

    public List<LoanOfferDTO> makeListLoanOfferDTO(LoanApplicationRequestDTO loanApplicationRequestDTO) {

        this.list.add(withoutAllOffer(new LoanOfferDTO(loanApplicationRequestDTO)));
        this.list.add(withSalaryOffer(new LoanOfferDTO(loanApplicationRequestDTO)));
        this.list.add(withInsuranceOffer(new LoanOfferDTO(loanApplicationRequestDTO)));
        this.list.add(withAllOffer(new LoanOfferDTO(loanApplicationRequestDTO)));
        log.info("Список с экземплярами LoanOfferDTO успешно создан: " + list);
        return list;
    }

    public LoanOfferDTO withoutAllOffer(LoanOfferDTO loanOfferDTO) {  //

        log.info("Произведено создание экземпляра класса LoanOfferDTO посредством вызова функции withoutAllOffer класса OffersService");
        loanOfferDTO.setApplicationId(OffersService.idLoadOfferDTO);
        loanOfferDTO.setRate(rate);
        loanOfferDTO.setTotalAmount(loanOfferDTO.getRequestedAmount());
        loanOfferDTO.setMonthlyPayment(calculateMonthlyPayment(loanOfferDTO));
        loanOfferDTO.setIsInsuranceEnabled(false);
        loanOfferDTO.setIsSalaryClient(false);
        return loanOfferDTO;
    }

    public LoanOfferDTO withSalaryOffer(LoanOfferDTO loanOfferDTO) {

        log.info("Произведено создание экземпляра класса LoanOfferDTO посредством вызова функции withSalaryOffer класса OffersService");
        loanOfferDTO.setApplicationId(OffersService.idLoadOfferDTO);
        loanOfferDTO.setRate(this.rate);
        loanOfferDTO.setTotalAmount(loanOfferDTO.getRequestedAmount());
        loanOfferDTO.setRate(loanOfferDTO.getRate().subtract(BigDecimal.ONE));
        loanOfferDTO.setMonthlyPayment(calculateMonthlyPayment(loanOfferDTO));
        loanOfferDTO.setIsInsuranceEnabled(false);
        loanOfferDTO.setIsSalaryClient(true);

        return loanOfferDTO;
    }

    public LoanOfferDTO withInsuranceOffer(LoanOfferDTO loanOfferDTO) {

        log.info("Произведено создание экземпляра класса LoanOfferDTO посредством вызова функции withInsuranceOffer класса OffersService");
        loanOfferDTO.setApplicationId(OffersService.idLoadOfferDTO);
        loanOfferDTO.setRate(rate);
        loanOfferDTO.setTotalAmount(loanOfferDTO.getRequestedAmount().multiply(BigDecimal.valueOf(1.03)).setScale(2, RoundingMode.HALF_UP));
        loanOfferDTO.setRate(loanOfferDTO.getRate().subtract(BigDecimal.valueOf(3)));
        loanOfferDTO.setMonthlyPayment(calculateMonthlyPayment(loanOfferDTO));
        loanOfferDTO.setIsInsuranceEnabled(true);
        loanOfferDTO.setIsSalaryClient(false);

        return loanOfferDTO;
    }

    public LoanOfferDTO withAllOffer(LoanOfferDTO loanOfferDTO) {

        log.info("Произведено создание экземпляра класса LoanOfferDTO посредством вызова функции withAllOffer класса OffersService");
        loanOfferDTO.setApplicationId(OffersService.idLoadOfferDTO);
        loanOfferDTO.setRate(rate);
        loanOfferDTO.setTotalAmount(loanOfferDTO.getRequestedAmount().multiply(BigDecimal.valueOf(1.03)).setScale(2, RoundingMode.HALF_UP));
        loanOfferDTO.setRate(loanOfferDTO.getRate().subtract(BigDecimal.valueOf(4)));
        loanOfferDTO.setMonthlyPayment(calculateMonthlyPayment(loanOfferDTO));
        loanOfferDTO.setIsInsuranceEnabled(true);
        loanOfferDTO.setIsSalaryClient(true);

        return loanOfferDTO;
    }

    public static BigDecimal calculateMonthlyPayment(LoanOfferDTO loanOfferDTO) {
        log.info("Вызвана функция calculateMonthlyPayment класса CalculatorOffers для исчисления ежемесячного платежа");
        return CalculatorOffers.calculateMonthlyPayment(loanOfferDTO.getTotalAmount(), loanOfferDTO.getTerm(), loanOfferDTO.getRate());
    }
}