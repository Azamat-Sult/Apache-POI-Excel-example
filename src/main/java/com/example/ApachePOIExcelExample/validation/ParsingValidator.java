package com.example.ApachePOIExcelExample.validation;

public interface ParsingValidator {

    boolean isValid(Object value);

    void validate(Object value) throws IllegalArgumentException;

}