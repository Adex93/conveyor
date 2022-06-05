package com.example.conveyor.controllers;

import com.example.conveyor.calculation.CreditDTO;
import com.example.conveyor.calculation.ScoringDataDTO;
import com.example.conveyor.calculation.ScoringService;
import com.example.conveyor.offers.LoanApplicationRequestDTO;
import com.example.conveyor.offers.LoanOfferDTO;
import com.example.conveyor.offers.OffersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.BindException;
import java.util.List;

@Slf4j
@RequestMapping("/conveyor")
@RestController
@Validated

public class MainController {

    final
    ApplicationContext context;

    public MainController(ApplicationContext context) {
        this.context = context;
    }

    @Tag(name = "The offers API", description = "Create LoanApplicationRequestDTO and get LoanOfferDTO")
    @PostMapping("/add-offer")


    public List<LoanOfferDTO> addNewOffer(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTOBody) {

        log.info("Произведено создание экземпляра класса LoanApplicationRequestDTO со следующими входными данными: " + loanApplicationRequestDTOBody);
        ++OffersService.idLoadOfferDTO;
        log.info("Вызвана функция makeListLoanOfferDTO класса OffersService для формирования списка с кредитными предложениями LoanOffersDTO");
        return context.getBean(OffersService.class).makeListLoanOfferDTO(loanApplicationRequestDTOBody);

    }


    @PostMapping("/calculating")
    @Tag(name = "The calculation API", description = "Create ScoringDataDTO and get CreditDTO")
    public CreditDTO calculating(@Valid @RequestBody ScoringDataDTO scoringDataDTO) throws BindException {

        log.info("Произведено создание экземпляра класса ScoringDataDTO со следующими входными данными: " + scoringDataDTO);
        log.info("Вызвана функция scoring класса ScoringService для произведения скоринга, произведения полного расчета параметров кредита и формирования CreditDTO");
        return context.getBean(ScoringService.class).scoring(scoringDataDTO);
    }


}
