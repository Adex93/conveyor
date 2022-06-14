package com.example.conveyor.calculation.service;

import com.example.conveyor.calculation.dto.CreditDTO;
import com.example.conveyor.calculation.dto.EmploymentDTO;
import com.example.conveyor.calculation.dto.PaymentScheduleElement;
import com.example.conveyor.calculation.dto.ScoringDataDTO;
import com.example.conveyor.calculation.enums.EmploymentStatus;
import com.example.conveyor.calculation.enums.Gender;
import com.example.conveyor.calculation.enums.MaritalStatus;
import com.example.conveyor.calculation.enums.Position;
import com.example.conveyor.offers.service.CalculatorOffers;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.BindException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@Component
public class ScoringService {

    @Value("${custom.calculating.baseRate}")
    BigDecimal rate;
    public CreditDTO scoring(ScoringDataDTO scoringDataDTO) throws BindException {
        CreditDTO creditDTO = new CreditDTO();
        EmploymentDTO employmentDTO = scoringDataDTO.getEmployment();
        int age = Period.between(scoringDataDTO.getBirthdate(), LocalDate.now()).getYears();

        if (!employmentDTO.getEmploymentStatus().equals(EmploymentStatus.UNEMPLOYED)
                && (employmentDTO.getSalary().multiply(BigDecimal.valueOf(30))).compareTo(scoringDataDTO.getAmount()) >= 0
                && employmentDTO.getWorkExperienceTotal().compareTo(12) >= 0
                && employmentDTO.getWorkExperienceCurrent().compareTo(3) >= 0
                && age >= 20 && age <= 65) {

            creditDTO.setRate(rate);
            creditDTO.setTerm(scoringDataDTO.getTerm());
            creditDTO.setAmount(scoringDataDTO.getAmount());
            creditDTO.setIsInsuranceEnabled(scoringDataDTO.getIsInsuranceEnabled());
            creditDTO.setIsSalaryClient(scoringDataDTO.getIsSalaryClient());
            if (employmentDTO.getEmploymentStatus().equals(EmploymentStatus.SELF_EMPLOYED)) {
                creditDTO.setRate(creditDTO.getRate().add(BigDecimal.ONE));
            } else if (employmentDTO.getEmploymentStatus().equals(EmploymentStatus.BUSINESSMAN)) {
                creditDTO.setRate(creditDTO.getRate().add(BigDecimal.valueOf(3)));
            }
            if (employmentDTO.getPosition().equals(Position.MID_MANAGER)) {
                creditDTO.setRate(creditDTO.getRate().subtract(BigDecimal.ONE));
            } else if (employmentDTO.getPosition().equals(Position.TOP_MANAGER)) {
                creditDTO.setRate(creditDTO.getRate().subtract(BigDecimal.valueOf(2)));
            }
            if (scoringDataDTO.getMaritalStatus().equals(MaritalStatus.MARRIED)) {
                creditDTO.setRate(creditDTO.getRate().subtract(BigDecimal.ONE));
            } else if (scoringDataDTO.getMaritalStatus().equals(MaritalStatus.SINGLE)) {
                creditDTO.setRate(creditDTO.getRate().add(BigDecimal.ONE));
            }
            if (scoringDataDTO.getDependentAmount() > 1) {
                creditDTO.setRate(creditDTO.getRate().add(BigDecimal.ONE));
            }
            if ((scoringDataDTO.getGender().equals(Gender.MALE) && age > 30 && age < 55)
                    || (scoringDataDTO.getGender().equals(Gender.FEMALE) && age > 35 && age < 60)) {
                creditDTO.setRate(creditDTO.getRate().subtract(BigDecimal.ONE));
            }
            if (creditDTO.getIsInsuranceEnabled() && creditDTO.getIsSalaryClient()) {
                creditDTO.setRate(creditDTO.getRate().subtract(BigDecimal.valueOf(4)));
            } else if (creditDTO.getIsInsuranceEnabled() && !creditDTO.getIsSalaryClient()) {
                creditDTO.setRate(creditDTO.getRate().subtract(BigDecimal.valueOf(3)));
            } else if (!creditDTO.getIsInsuranceEnabled() && creditDTO.getIsSalaryClient()) {
                creditDTO.setRate(creditDTO.getRate().subtract(BigDecimal.ONE));
            }
            if (creditDTO.getIsInsuranceEnabled()) {
                creditDTO.setAmount(creditDTO.getAmount().multiply(BigDecimal.valueOf(1.030)));
            }

            log.info("Вызвана функция calculateMonthlyPayment класса CalculatorOffers для исчисления ежемесячного платежа");
            creditDTO.setMonthlyPayment(CalculatorOffers.calculateMonthlyPayment(creditDTO.getAmount(), creditDTO.getTerm(), creditDTO.getRate()));

            log.info("Вызвана функция calculatePSK класса CalculatorCredit для полной стоимости кредита");
            creditDTO.setPsk(CalculatorCredit.calculatePSK(creditDTO.getMonthlyPayment(), scoringDataDTO.getAmount(), creditDTO.getTerm()));

            log.info("Вызвана функция createListPayment класса ScoringService для формирования графика платежей");

            creditDTO.setPaymentSchedule(createListPayment(creditDTO));

            log.info("Скоринг, полный расчет параметров кредиа,формирование CreditDTO успешно произведены: " + creditDTO);
            return creditDTO;
        } else {

            log.error("Скорринг не пройден");
            throw new BindException("Скорринг не пройден");
        }
    }

    public static List<PaymentScheduleElement> createListPayment(CreditDTO creditDTO) {

        List<PaymentScheduleElement> paymentScheduleElementsList = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        BigDecimal amount = creditDTO.getAmount();

        for (int i = 1; i <= creditDTO.getTerm(); i++) {

            PaymentScheduleElement paymentScheduleElement = new PaymentScheduleElement();
            paymentScheduleElement.setNumber(i);
            localDate = localDate.plusMonths(1);
            paymentScheduleElement.setDate(localDate);
            paymentScheduleElement.setTotalPayment(creditDTO.getMonthlyPayment());
            paymentScheduleElement.setInterestPayment(CalculatorCredit.calculateRatePayment(creditDTO.getRate(), amount));
            paymentScheduleElement.setDebtPayment(CalculatorCredit.calculateBodyPayment(paymentScheduleElement.getInterestPayment(), paymentScheduleElement.getTotalPayment()));
            amount = amount.subtract(paymentScheduleElement.getDebtPayment());
            paymentScheduleElement.setRemainingDebt(amount);
            paymentScheduleElementsList.add(paymentScheduleElement);
        }

        return paymentScheduleElementsList;

    }
}

