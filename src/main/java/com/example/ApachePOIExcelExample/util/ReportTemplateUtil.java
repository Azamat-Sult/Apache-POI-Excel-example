package com.example.ApachePOIExcelExample.util;

import com.example.ApachePOIExcelExample.annotation.ReportFieldControl;
import com.example.ApachePOIExcelExample.annotation.ReportFieldControl.AccessType;
import com.example.ApachePOIExcelExample.annotation.ReportHeader;
import com.example.ApachePOIExcelExample.model.ReportFieldsDto;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;

public class ReportTemplateUtil {

    public static String getFieldName(Field field) {
        return Optional.ofNullable(field.getAnnotation(ReportHeader.class))
                .map(ReportHeader::value)
                .orElse(field.getName());
    }

    public static <T> List<ReportFieldsDto> getFieldsByAccessType(Class<T> template, AccessType accessType) {
        List<ReportFieldsDto> result = new ArrayList<>();
        var fields = template.getDeclaredFields();
        for (var field : fields) {
            var fieldAccessType = getFieldAccessType(field);
            var fieldName = getFieldName(field);
            if (accessType.equals(fieldAccessType)) {
                result.add(new ReportFieldsDto(field.getName(), fieldName));
            }
        }
        return result;
    }

    private static AccessType getFieldAccessType(Field field) {
        return Optional.ofNullable(field.getAnnotation(ReportFieldControl.class))
                .map(ReportFieldControl::accessType)
                .orElse(AccessType.SHOW_ALWAYS);
    }

    public static <T> List<String> getLinkedFields(Class<T> template, List<String> links) {
        List<String> result = new ArrayList<>();
        var fields = template.getDeclaredFields();
        for (var field : fields) {
            var access = field.getAnnotation(ReportFieldControl.class);
            if (Objects.nonNull(access)
                    && access.accessType().equals(AccessType.PART_OF)
                    && Arrays.stream(access.linkedTo()).anyMatch(links::contains)) {
                result.add(field.getName());
            }
        }
        return result;
    }

    public static <T> Field[] filterSelectedFields(Class<T> template, List<String> selectedFields) {
        var allFields = Arrays.stream(template.getDeclaredFields())
                .filter(field -> !AccessType.TECH.equals(getFieldAccessType(field)));

        if (CollectionUtils.isEmpty(selectedFields)) {
            return allFields.filter(field -> !AccessType.HIDDEN.equals(getFieldAccessType(field)))
                    .toArray(Field[]::new);
        } else {
            return allFields.filter(field -> selectedFields.contains(field.getName()))
                    .toArray(Field[]::new);
        }
    }

}