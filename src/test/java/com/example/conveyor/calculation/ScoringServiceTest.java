package com.example.conveyor.calculation;

import com.example.conveyor.calculation.dto.CreditDTO;
import com.example.conveyor.calculation.dto.EmploymentDTO;
import com.example.conveyor.calculation.dto.PaymentScheduleElement;
import com.example.conveyor.calculation.dto.ScoringDataDTO;
import com.example.conveyor.calculation.enums.EmploymentStatus;
import com.example.conveyor.calculation.enums.Gender;
import com.example.conveyor.calculation.enums.MaritalStatus;
import com.example.conveyor.calculation.enums.Position;
import com.example.conveyor.calculation.service.ScoringService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.net.BindException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {

    @Test
    void scoringShouldReturnCreditDTO() throws BindException {
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        ScoringService scoringService = new ScoringService();
        EmploymentDTO employmentDTO = new EmploymentDTO();

        scoringService.setRate(BigDecimal.valueOf(20));
        scoringDataDTO.setAmount(BigDecimal.valueOf(2000000));
        scoringDataDTO.setTerm(24);
        scoringDataDTO.setGender(Gender.MALE);
        scoringDataDTO.setBirthdate(LocalDate.of(1993, 7, 28));
        scoringDataDTO.setMaritalStatus(MaritalStatus.SINGLE);
        scoringDataDTO.setDependentAmount(0);
        scoringDataDTO.setIsInsuranceEnabled(true);
        scoringDataDTO.setIsSalaryClient(true);
        employmentDTO.setEmploymentStatus(EmploymentStatus.EMPLOYED);
        employmentDTO.setSalary(BigDecimal.valueOf(100000));
        employmentDTO.setPosition(Position.MID_MANAGER);
        employmentDTO.setWorkExperienceTotal(72);
        employmentDTO.setWorkExperienceCurrent(24);
        scoringDataDTO.setEmployment(employmentDTO);

        CreditDTO creditDTO = scoringService.scoring(scoringDataDTO);

        assertEquals(BigDecimal.valueOf(16), creditDTO.getRate());
    }

    @Test
    void createListPaymentShouldReturnException() {
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        ScoringService scoringService = new ScoringService();
        EmploymentDTO employmentDTO = new EmploymentDTO();

        scoringDataDTO.setBirthdate(LocalDate.of(2013, 7, 28));
        employmentDTO.setEmploymentStatus(EmploymentStatus.UNEMPLOYED);
        employmentDTO.setSalary(BigDecimal.valueOf(100));
        employmentDTO.setWorkExperienceTotal(2);
        employmentDTO.setWorkExperienceCurrent(2);
        scoringDataDTO.setEmployment(employmentDTO);

        Throwable throwable = assertThrows(BindException.class, () -> {
            scoringService.scoring(scoringDataDTO);
        });

        assertEquals("Скорринг не пройден", throwable.getMessage());

    }

    @Test
    void createListPaymentShouldReturnList() {
        List<PaymentScheduleElement> paymentScheduleElementsList;
        CreditDTO creditDTO = new CreditDTO();

        creditDTO.setAmount(BigDecimal.valueOf(2060000));
        creditDTO.setMonthlyPayment(BigDecimal.valueOf(100864.01));
        creditDTO.setRate(BigDecimal.valueOf(16));
        creditDTO.setTerm(24);
        paymentScheduleElementsList = ScoringService.createListPayment(creditDTO);

        assertNotNull(paymentScheduleElementsList);
        assertEquals(24, paymentScheduleElementsList.size());
        assertEquals(24, paymentScheduleElementsList.size());
        assertEquals(1, paymentScheduleElementsList.get(0).getNumber());
        assertEquals(LocalDate.now().plusMonths(1), paymentScheduleElementsList.get(0).getDate());
        assertEquals(BigDecimal.valueOf(100864.01), paymentScheduleElementsList.get(0).getTotalPayment());
        assertEquals(BigDecimal.valueOf(27466.67), paymentScheduleElementsList.get(0).getInterestPayment());
        assertEquals(BigDecimal.valueOf(73397.34), paymentScheduleElementsList.get(0).getDebtPayment());
        assertEquals(BigDecimal.valueOf(1986602.66), paymentScheduleElementsList.get(0).getRemainingDebt());


    }
}