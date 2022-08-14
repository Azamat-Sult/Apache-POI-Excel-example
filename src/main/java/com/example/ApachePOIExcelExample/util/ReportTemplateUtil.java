package com.example.ApachePOIExcelExample.util;

import com.example.ApachePOIExcelExample.annotation.ReportHeader;

import java.lang.reflect.Field;
import java.util.Optional;

public class ReportTemplateUtil {

    public static String getFieldName(Field field) {
        return Optional.ofNullable(field.getAnnotation(ReportHeader.class))
                .map(ReportHeader::value)
                .orElse(field.getName());
    }

}