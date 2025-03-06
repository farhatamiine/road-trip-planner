package com.amine.roadtripplanner.annotation.DateFormatValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ValidDateFormatConstraintValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ValidDateFormat {
    String message() default "Invalid date format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
