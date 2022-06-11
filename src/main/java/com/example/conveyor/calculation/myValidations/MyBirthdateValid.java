package com.example.conveyor.calculation.myValidations;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.Period;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = BirthdateValidator.class)
@Documented
public @interface MyBirthdateValid {

    String message() default "{ Age less than 18 years }";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

class BirthdateValidator implements ConstraintValidator<MyBirthdateValid, LocalDate> {


    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        if (localDate!=null){
            return Period.between(localDate, LocalDate.now()).getYears() >= 18;
        } else return false;
    }
}
