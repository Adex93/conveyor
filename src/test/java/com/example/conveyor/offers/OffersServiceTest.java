package com.example.conveyor.offers;

import com.example.conveyor.offers.dto.LoanApplicationRequestDTO;
import com.example.conveyor.offers.dto.LoanOfferDTO;
import com.example.conveyor.offers.service.OffersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OffersServiceTest {

    LoanApplicationRequestDTO loanApplicationRequestDTO = new LoanApplicationRequestDTO();

    @BeforeEach
    void setLoanApplicationRequestDTO() {
        loanApplicationRequestDTO.setAmount(BigDecimal.valueOf(2000000));
        loanApplicationRequestDTO.setTerm(24);
    }


    @Test
    void makeListLoanOfferDTOShouldReturnListLoanDTO() {
        List<LoanOfferDTO> list = new ArrayList<>();
        list.add(new LoanOfferDTO(loanApplicationRequestDTO));
        list.add(new LoanOfferDTO(loanApplicationRequestDTO));
        list.add(new LoanOfferDTO(loanApplicationRequestDTO));
        list.add(new LoanOfferDTO(loanApplicationRequestDTO));

        assertEquals(4, list.size());
        assertNotNull(list.get(0));
        assertNotNull(list.get(1));
        assertNotNull(list.get(2));
        assertNotNull(list.get(3));
    }

    @Test
    void withoutAllOfferShouldReturnLoanDTO() {
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO(loanApplicationRequestDTO);
        OffersService offersService = new OffersService();
        offersService.setRate(BigDecimal.valueOf(20));

        loanOfferDTO = offersService.withoutAllOffer(loanOfferDTO);

        assertNotNull(loanOfferDTO);
        assertEquals(BigDecimal.valueOf(2000000), loanOfferDTO.getRequestedAmount());
        assertEquals(BigDecimal.valueOf(2000000), loanOfferDTO.getTotalAmount());
        assertEquals(BigDecimal.valueOf(20), loanOfferDTO.getRate());
        assertEquals(24, loanOfferDTO.getTerm());
        assertEquals(BigDecimal.valueOf(101791.61), loanOfferDTO.getMonthlyPayment());
        assertFalse(loanOfferDTO.getIsInsuranceEnabled());
        assertFalse(loanOfferDTO.getIsSalaryClient());
    }

    @Test
    void withSalaryOfferShouldReturnLoanDTO() {
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO(loanApplicationRequestDTO);
        OffersService offersService = new OffersService();
        offersService.setRate(BigDecimal.valueOf(20));

        loanOfferDTO = offersService.withSalaryOffer(loanOfferDTO);

        assertNotNull(loanOfferDTO);
        assertEquals(BigDecimal.valueOf(2000000), loanOfferDTO.getRequestedAmount());
        assertEquals(BigDecimal.valueOf(2000000), loanOfferDTO.getTotalAmount());
        assertEquals(BigDecimal.valueOf(19), loanOfferDTO.getRate());
        assertEquals(24, loanOfferDTO.getTerm());
        assertEquals(BigDecimal.valueOf(100817.23), loanOfferDTO.getMonthlyPayment());
        assertFalse(loanOfferDTO.getIsInsuranceEnabled());
        assertTrue(loanOfferDTO.getIsSalaryClient());
    }

    @Test
    void withInsuranceOfferShouldReturnLoanDTO() {
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO(loanApplicationRequestDTO);
        OffersService offersService = new OffersService();
        offersService.setRate(BigDecimal.valueOf(20));

        loanOfferDTO = offersService.withInsuranceOffer(loanOfferDTO);

        assertNotNull(loanOfferDTO);
        assertEquals(BigDecimal.valueOf(2000000), loanOfferDTO.getRequestedAmount());
        assertEquals(BigDecimal.valueOf(2060000).setScale(2), loanOfferDTO.getTotalAmount());
        assertEquals(BigDecimal.valueOf(17), loanOfferDTO.getRate());
        assertEquals(24, loanOfferDTO.getTerm());
        assertEquals(BigDecimal.valueOf(101851.06), loanOfferDTO.getMonthlyPayment());
        assertTrue(loanOfferDTO.getIsInsuranceEnabled());
        assertFalse(loanOfferDTO.getIsSalaryClient());
    }

    @Test
    void withAllOfferShouldReturnLoanDTO() {

        LoanOfferDTO loanOfferDTO = new LoanOfferDTO(loanApplicationRequestDTO);
        OffersService offersService = new OffersService();
        offersService.setRate(BigDecimal.valueOf(20));

        loanOfferDTO = offersService.withAllOffer(loanOfferDTO);

        assertNotNull(loanOfferDTO);
        assertEquals(BigDecimal.valueOf(2000000), loanOfferDTO.getRequestedAmount());
        assertEquals(BigDecimal.valueOf(2060000).setScale(2), loanOfferDTO.getTotalAmount());
        assertEquals(24, loanOfferDTO.getTerm());
        assertEquals(BigDecimal.valueOf(100864.01), loanOfferDTO.getMonthlyPayment());
        assertEquals(BigDecimal.valueOf(16), loanOfferDTO.getRate());
        assertTrue(loanOfferDTO.getIsInsuranceEnabled());
        assertTrue(loanOfferDTO.getIsSalaryClient());
    }

    @Test
    void calculateMonthlyPaymentShouldReturnBigDecimal() {

        LoanOfferDTO loanOfferDTO = new LoanOfferDTO(loanApplicationRequestDTO);
        loanOfferDTO.setTotalAmount(loanApplicationRequestDTO.getAmount());
        loanOfferDTO.setRate(BigDecimal.valueOf(20));

        BigDecimal expected = BigDecimal.valueOf(101791.61);
        BigDecimal actual = OffersService.calculateMonthlyPayment(loanOfferDTO);

        assertEquals(expected, actual);
    }
}