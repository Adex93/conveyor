package com.example.conveyor.controllers;

import com.example.conveyor.calculation.EmploymentDTO;
import com.example.conveyor.calculation.ScoringDataDTO;
import com.example.conveyor.calculation.ScoringService;
import com.example.conveyor.offers.LoanApplicationRequestDTO;
import com.example.conveyor.offers.OffersService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.context.ApplicationContext;
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

    final
    ApplicationContext context;

    public MainControllerTest(ApplicationContext context) {
        this.context = context;
    }


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;


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


        mockMvc.perform(MockMvcRequestBuilders.post("/conveyor/add-offer")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertNotNull(context.getBean(OffersService.class).makeListLoanOfferDTO(loanApplicationRequestDTO));
        assertEquals(4, context.getBean(OffersService.class).makeListLoanOfferDTO(loanApplicationRequestDTO).size());

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
        scoringDataDTO.setGender(ScoringDataDTO.Gender.MALE);
        scoringDataDTO.setBirthdate(LocalDate.of(1993, 7, 28));
        scoringDataDTO.setPassportSeries("1234");
        scoringDataDTO.setPassportNumber("123456");
        scoringDataDTO.setPassportIssueDate(LocalDate.of(2015, 5, 15));
        scoringDataDTO.setPassportIssueBranch("360-018");
        scoringDataDTO.setMaritalStatus(ScoringDataDTO.MaritalStatus.SINGLE);
        scoringDataDTO.setDependentAmount(0);
        scoringDataDTO.setAccount("40702810400000123456");
        scoringDataDTO.setIsInsuranceEnabled(true);
        scoringDataDTO.setIsSalaryClient(true);
        employmentDTO.setEmploymentStatus(EmploymentDTO.EmploymentStatus.EMPLOYED);
        employmentDTO.setEmployerINN("770712083893");
        employmentDTO.setSalary(BigDecimal.valueOf(100000));
        employmentDTO.setPosition(EmploymentDTO.Position.MID_MANAGER);
        employmentDTO.setWorkExperienceTotal(72);
        employmentDTO.setWorkExperienceCurrent(24);


        scoringDataDTO.setEmployment(employmentDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/conveyor/calculating")
                        .content(objectMapper.writeValueAsString(scoringDataDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertNotNull(context.getBean(ScoringService.class).scoring(scoringDataDTO));
    }
}
