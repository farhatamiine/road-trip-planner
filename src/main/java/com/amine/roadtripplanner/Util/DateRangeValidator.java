package com.amine.roadtripplanner.Util;

import com.amine.roadtripplanner.Entities.Segment;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Segment> {


    @Override
    public boolean isValid(Segment segment, ConstraintValidatorContext constraintValidatorContext) {

        if (segment.getStartTime() == null || segment.getEndTime() == null) {
            return true;
        }
        if (!segment.getStartTime().isBefore(segment.getEndTime())) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    constraintValidatorContext.getDefaultConstraintMessageTemplate()
                            .replace("{startTime}", segment.getStartTime().toString())
                            .replace("{endTime}", segment.getEndTime().toString())
            ).addConstraintViolation();
            return false;
        }
        return true;

    }
}
