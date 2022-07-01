package com.example.conveyor.controllers;

import com.example.conveyor.calculation.dto.EmploymentDTO;
import com.example.conveyor.calculation.dto.ScoringDataDTO;
import com.example.conveyor.calculation.enums.EmploymentStatus;
import com.example.conveyor.calculation.enums.Gender;
import com.example.conveyor.calculation.enums.MaritalStatus;
import com.example.conveyor.calculation.enums.Position;
import com.example.conveyor.calculation.service.ScoringService;
import com.example.conveyor.offers.dto.LoanApplicationRequestDTO;
import com.example.conveyor.offers.service.OffersService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    OffersService offersService;
    @Autowired
    ScoringService scoringService;



    @Test
    void addNewOfferShouldReturnListLianOfferDTO() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = new LoanApplicationRequestDTO();
        loanApplicationRequestDTO.setTerm(24);
        loanApplicationRequestDTO.setAmount(BigDecimal.valueOf(200000));
        loanApplicationRequestDTO.setFirstName("Aleksandr");
        loanApplicationRequestDTO.setLastName("Dmitriev");
        loanApplicationRequestDTO.setMiddleName("Sergeevich");
        loanApplicationRequestDTO.setEmail("dmitriev_alexandr93@mail.ru");
        loanApplicationRequestDTO.setBirthdate(LocalDate.of(1993, 7, 28));
        loanApplicationRequestDTO.setPassportSeries("1234");
        loanApplicationRequestDTO.setPassportNumber("123456");


        mockMvc.perform(MockMvcRequestBuilders.post("/conveyor/offers")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertNotNull(offersService.makeListLoanOfferDTO(loanApplicationRequestDTO));
        assertEquals(4, offersService.makeListLoanOfferDTO(loanApplicationRequestDTO).size());

    }

    @Test
    void calculating() throws Exception {
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        EmploymentDTO employmentDTO = new EmploymentDTO();
        ScoringService scoringService = new ScoringService();
        scoringService.setRate(BigDecimal.valueOf(20));

        scoringDataDTO.setAmount(BigDecimal.valueOf(2000000));
        scoringDataDTO.setTerm(24);
        scoringDataDTO.setFirstName("Aleksandr");
        scoringDataDTO.setLastName("Dmitriev");
        scoringDataDTO.setMiddleName("Sergeevich");
        scoringDataDTO.setGender(Gender.MALE);
        scoringDataDTO.setBirthdate(LocalDate.of(1993, 7, 28));
        scoringDataDTO.setPassportSeries("1234");
        scoringDataDTO.setPassportNumber("123456");
        scoringDataDTO.setPassportIssueDate(LocalDate.of(2015, 5, 15));
        scoringDataDTO.setPassportIssueBranch("360-018");
        scoringDataDTO.setMaritalStatus(MaritalStatus.SINGLE);
        scoringDataDTO.setDependentAmount(0);
        scoringDataDTO.setAccount("40702810400000123456");
        scoringDataDTO.setIsInsuranceEnabled(true);
        scoringDataDTO.setIsSalaryClient(true);
        employmentDTO.setEmploymentStatus(EmploymentStatus.EMPLOYED);
        employmentDTO.setEmployerINN("770712083893");
        employmentDTO.setSalary(BigDecimal.valueOf(100000));
        employmentDTO.setPosition(Position.MID_MANAGER);
        employmentDTO.setWorkExperienceTotal(72);
        employmentDTO.setWorkExperienceCurrent(24);

        scoringDataDTO.setEmployment(employmentDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/conveyor/calculation")
                        .content(objectMapper.writeValueAsString(scoringDataDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertNotNull(scoringService.scoring(scoringDataDTO));
    }
}
