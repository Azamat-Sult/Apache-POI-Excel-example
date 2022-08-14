package com.example.ApachePOIExcelExample.annotation;

import com.example.ApachePOIExcelExample.validation.StringValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@ParsingValidate(validatedBy = {StringValidator.class})
public @interface ValidString {

}