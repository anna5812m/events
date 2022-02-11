package com.example.events.model;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class RepetitionValidationErrorBuilder {
    public static RepetitionValidationError fromBindingErrors(Errors errors) {
        RepetitionValidationError error = new RepetitionValidationError("Validation failed. " + errors.getErrorCount() + " error(s)");
        for (ObjectError objectError : errors.getAllErrors()) {
            error.addValidationError(objectError.getDefaultMessage());
        }
        return error;
    }
}
