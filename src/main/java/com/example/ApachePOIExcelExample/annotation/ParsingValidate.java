package com.example.ApachePOIExcelExample.annotation;

import com.example.ApachePOIExcelExample.validation.ParsingValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface ParsingValidate {

    Class<? extends ParsingValidator>[] validatedBy();

}