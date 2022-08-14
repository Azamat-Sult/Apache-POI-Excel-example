package com.example.ApachePOIExcelExample.validation;

import com.example.ApachePOIExcelExample.util.RegularExpressions;

public class NumberValidator implements ParsingValidator {

    @Override
    public boolean isValid(Object value) {
        return value.toString().matches(RegularExpressions.NUMBER_SIMPLE);
    }

    @Override
    public void validate(Object value) throws IllegalArgumentException {
        if (!isValid(value)) {
            throw new IllegalArgumentException(String.format(
                    "Значение \"%s\" не соответствует заданному шаблону \"%s\"",
                    value, RegularExpressions.NUMBER_SIMPLE));
        }
    }

}