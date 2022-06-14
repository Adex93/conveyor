package com.example.conveyor.offers.dto;

import com.example.conveyor.calculation.myValidations.MyBirthdateValid;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Getter
@Setter
@ToString
public class LoanApplicationRequestDTO {

    @NotNull
    @Min(value = 6, message = "Tern should be greater than 6")
    @Schema(description = "Срок кредита в месяцах", example = "24")
    Integer term;

    @NotNull
    @Min(value = 10000, message = "Amount should be greater than 10000")
    @Schema(description = "Запрашиваемая сумма денежных средств", example = "2000000")
    BigDecimal amount;


    @Pattern(regexp = "^[A-Za-z]+$")
    @Size(min = 2, max = 30, message = "FirstName should be between 2 and 30 characters")
    @NotBlank(message = "FirstName should not be empty")
    @Schema(description = "Имя", example = "Aleksandr")
    String firstName;

    @Pattern(regexp = "^[A-Za-z]+$")
    @Size(min = 2, max = 30, message = "lastName should be between 2 and 30 characters")
    @NotBlank(message = "lastName should not be empty")
    @Schema(description = "Фамилия", example = "Dmitriev")
    String lastName;

    @Pattern(regexp = "^[A-Za-z]+$")
    @Size(min = 2, max = 30, message = "MiddleName should be between 2 and 30 characters or be Null")
    @Schema(description = "Отчество (при наличии)", example = "Sergeevich")
    String middleName;

    @NotBlank
    @Email(message = "Email is uncorrected")
    @Schema(description = "Электронная почта", example = "dmitriev_alexandr93@mail.ru")
    String email;

    @NotNull
    @MyBirthdateValid
    @Past(message = "Birthdate is uncorrected")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Дата рождения", example = "1993-07-28")
    LocalDate birthdate;

    @Pattern(regexp = "^\\d+$", message = "PassportSeries is uncorrected")
    @NotBlank
    @Size(min = 4, max = 4, message = "PassportSeries is uncorrected")
    @Schema(description = "Серия паспорта", example = "1234")
    String passportSeries;

    @Pattern(regexp = "^\\d+$", message = "PassportNumber is uncorrected")
    @NotBlank
    @Size(min = 6, max = 6, message = "PassportNumber is uncorrected")
    @Schema(description = "Номер паспорта", example = "123456")
    String passportNumber;


}





