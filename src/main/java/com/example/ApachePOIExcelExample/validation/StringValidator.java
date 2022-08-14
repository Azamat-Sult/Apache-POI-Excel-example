package com.example.ApachePOIExcelExample.validation;

import com.example.ApachePOIExcelExample.util.RegularExpressions;

public class StringValidator implements ParsingValidator {

    @Override
    public boolean isValid(Object value) {
        return value.toString().matches(RegularExpressions.ENGLISH_SYMBOLS_ONLY);
    }

    @Override
    public void validate(Object value) throws IllegalArgumentException {
        if (!isValid(value)) {
            throw new IllegalArgumentException(String.format(
                    "Значение \"%s\" не соответствует заданному шаблону \"%s\"",
                    value, RegularExpressions.ENGLISH_SYMBOLS_ONLY));
        }
    }

}