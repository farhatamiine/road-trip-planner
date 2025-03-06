package com.amine.roadtripplanner.annotation.DateFormatValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class ValidDateFormatConstraintValidator implements ConstraintValidator<ValidDateFormat, LocalDate> {


    private static final String DATE_PATTERN = "MM/dd/yyyy";

    @Override
    public boolean isValid(LocalDate tripDate, ConstraintValidatorContext constraintValidatorContext) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        try {
            sdf.setLenient(false);
            sdf.parse(String.valueOf(tripDate));
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
