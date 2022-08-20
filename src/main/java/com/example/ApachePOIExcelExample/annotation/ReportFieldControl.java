package com.example.ApachePOIExcelExample.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReportFieldControl {

    AccessType accessType() default AccessType.SHOW_ALWAYS;

    enum AccessType {
        SHOW_ALWAYS,
        IF_ASKED,
        PART_OF,
        HIDDEN,
        TECH

    }

    String[] linkedTo() default {};

}