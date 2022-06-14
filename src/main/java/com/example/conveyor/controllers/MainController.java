package com.example.conveyor.controllers;

import com.example.conveyor.calculation.dto.CreditDTO;
import com.example.conveyor.calculation.dto.ScoringDataDTO;
import com.example.conveyor.calculation.service.ScoringService;
import com.example.conveyor.offers.dto.LoanApplicationRequestDTO;
import com.example.conveyor.offers.dto.LoanOfferDTO;
import com.example.conveyor.offers.service.OffersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.BindException;
import java.util.List;

@Slf4j
@RestController
@Validated
public class MainController {

    final
    OffersService offersService;

    final
    ScoringService scoringService;

    public MainController(OffersService offersService, ScoringService scoringService) {
        this.offersService = offersService;
        this.scoringService = scoringService;
    }

    @Tag(name = "The offers API", description = "Create LoanApplicationRequestDTO and get LoanOfferDTO")
    @PostMapping("/conveyor/offers")
    public ResponseEntity<List<LoanOfferDTO>> addNewOffer(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTOBody) {

        log.info("Произведено создание экземпляра класса LoanApplicationRequestDTO со следующими входными данными: " + loanApplicationRequestDTOBody);
        log.info("Вызвана функция makeListLoanOfferDTO класса OffersService для формирования списка с кредитными предложениями LoanOffersDTO");

        return new ResponseEntity<>(offersService.makeListLoanOfferDTO(loanApplicationRequestDTOBody), HttpStatus.OK);
    }


    @PostMapping("/conveyor/calculation")
    @Tag(name = "The calculation API", description = "Create ScoringDataDTO and get CreditDTO")
    public ResponseEntity<CreditDTO> calculating(@Valid @RequestBody ScoringDataDTO scoringDataDTO) throws BindException {

        log.info("Произведено создание экземпляра класса ScoringDataDTO со следующими входными данными: " + scoringDataDTO);
        log.info("Вызвана функция scoring класса ScoringService для произведения скоринга, произведения полного расчета параметров кредита и формирования CreditDTO");

        return new ResponseEntity<>(scoringService.scoring(scoringDataDTO), HttpStatus.OK);
    }


}
