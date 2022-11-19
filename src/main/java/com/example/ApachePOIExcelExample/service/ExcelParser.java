package com.example.ApachePOIExcelExample.service;

import com.example.ApachePOIExcelExample.annotation.ParsingValidate;
import com.example.ApachePOIExcelExample.annotation.ReportTemplate;
import com.example.ApachePOIExcelExample.enums.EnumWithDescription;
import com.example.ApachePOIExcelExample.exception.ParsingException;
import com.example.ApachePOIExcelExample.util.ReportTemplateUtil;
import com.example.ApachePOIExcelExample.validation.ParsingValidator;
import org.apache.commons.collections4.IterableUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

public class ExcelParser<E> {

    private final Class<E> template;
    private final Field[] fields;
    private final Map<Integer, Field> fieldsColumnsMap;

    public ExcelParser(Class<E> template) {
        this.template = template;
        this.fields = template.getDeclaredFields();
        this.fieldsColumnsMap = new HashMap<>();
    }

    public List<E> parse(MultipartFile file, boolean hasHeader, boolean ignoreHeader) throws ParsingException {
        try (var xWorkbook = new XSSFWorkbook(file.getInputStream())) {

            var xSheet = getSheet(xWorkbook);
            var skipHeader = hasHeader ? 1 : 0;

            if (hasHeader && !ignoreHeader) {
                var headerRow = xSheet.getRow(0);
                prepareValidatorsByHeader(headerRow);
            } else {
                prepareValidatorsByIndex();
            }

            var dataSet = new ArrayList<E>();
            var rowNumber = 0;

            for (Row row : xSheet) {
                XSSFRow xRow = (XSSFRow) row;
                if (rowNumber >= skipHeader) {
                    var data = parseRow(xRow);
                    if (Objects.nonNull(data)) {
                        dataSet.add(data);
                    }
                }
                rowNumber++;
            }

            return dataSet;

        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private XSSFSheet getSheet(XSSFWorkbook workbook) throws IllegalArgumentException {
        var reportTemplate = template.getAnnotation(ReportTemplate.class);
        XSSFSheet sheet;
        if (Objects.isNull(reportTemplate)) {
            sheet = workbook.getSheetAt(0);
        } else {
            sheet = workbook.getSheet(reportTemplate.name());
            if (Objects.isNull(sheet)) {
                throw new IllegalArgumentException(String.format("Лист \"%s\" не найден", reportTemplate.name()));
            }
        }
        return sheet;
    }

    private void prepareValidatorsByHeader(XSSFRow headerRow) {
        var columnsMap = new HashMap<String, Integer>();
        var headers = IterableUtils.toList(headerRow);

        for (var header : headers) {
            var column = header.getColumnIndex();
            var label = header.getStringCellValue();
            columnsMap.put(label, column);
        }

        for (var field : fields) {
            var label = ReportTemplateUtil.getFieldName(field);
            if (columnsMap.containsKey(label)) {
                fieldsColumnsMap.put(columnsMap.get(label), field);
            }
        }
    }

    private void prepareValidatorsByIndex() {
        var column = 0;
        for (var field : fields) {
            fieldsColumnsMap.put(column++, field);
        }
    }

    private E parseRow(XSSFRow xRow) {

        try {

            var constructor = template.getDeclaredConstructor();
            E row = constructor.newInstance();
            var isEmpty = true;

            for (Map.Entry<Integer, Field> entry : fieldsColumnsMap.entrySet()) {
                var column = entry.getKey();
                var field = entry.getValue();

                var xCell = xRow.getCell(column);
                if (Objects.nonNull(xCell)) {
                    var value = parseField(row, field, xCell);
                    if (Objects.nonNull(value)) {
                        isEmpty = false;
                    }
                }
            }

            return isEmpty ? null : row;

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new ParsingException(e);
        } catch (InvocationTargetException e) {
            throw new ParsingException(e.getTargetException().getMessage());
        }
    }

    private Object parseField(E row, Field field, XSSFCell xCell)
            throws InvocationTargetException, NoSuchMethodException,
            IllegalAccessException, InstantiationException {

        var value = getCellValue(xCell, field.getType());
        if (EnumWithDescription.class.isAssignableFrom(field.getType())) {
            if (Objects.nonNull(value)) {
                var label = value.toString();
                value = getEnumByDescription(field, label);
            }
        } else if (Objects.nonNull(value)) {
            validateValue(field, value);
        }
        return setFieldValue(row, field, value);
    }

    private Object getCellValue(XSSFCell xCell, Class<?> targetType) {
        CellType cellType = xCell.getCellType();
        switch (cellType) {
            case BLANK:
            case STRING:
            case FORMULA:
                return getNonEmpty(xCell.getStringCellValue());
            case NUMERIC:
                if (String.class.isAssignableFrom(targetType)) {
                    return getNonEmpty(xCell.getRawValue());
                } else {
                    return xCell.getNumericCellValue();
                }
            case BOOLEAN:
                return xCell.getBooleanCellValue();
            default:
                return null;
        }
    }

    private String getNonEmpty(String str) {
        return StringUtils.hasText(str) ? str : null;
    }

    private EnumWithDescription getEnumByDescription(Field field, String description)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var method = field.getType().getDeclaredMethod("getByDescription", String.class);
        return (EnumWithDescription) method.invoke(field, description);
    }

    private void validateValue(Field field, Object value)
            throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {

        var parsingValidate = field.getAnnotation(ParsingValidate.class);
        invokeValidator(parsingValidate, value);

        var annotations = field.getDeclaredAnnotations();
        for (var annotation : annotations) {
            parsingValidate = AnnotationUtils.findAnnotation(annotation.getClass(), ParsingValidate.class);
            invokeValidator(parsingValidate, value);
        }
    }

    private void invokeValidator(ParsingValidate annotation, Object value)
            throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {

        if (Objects.isNull(annotation)) {
            return;
        }
        for (var aClass : annotation.validatedBy()) {
            var constructor = aClass.getDeclaredConstructor();
            ParsingValidator validator = constructor.newInstance();
            validator.validate(value);
        }

    }

    private Object setFieldValue(E data, Field field, Object value)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        if (Objects.isNull(value)) {
            return null;
        }

        var methodName = getSetterMethodNameForField(field);
        var m = data.getClass().getDeclaredMethod(methodName, field.getType());

        Object castValue;
        if (value instanceof Double) {
            if (Long.class.isAssignableFrom(field.getType())) {
                castValue = ((Double) value).longValue();
            } else if (Integer.class.isAssignableFrom(field.getType())) {
                castValue = ((Double) value).intValue();
            } else if (BigDecimal.class.isAssignableFrom(field.getType())) {
                castValue = BigDecimal.valueOf((Double) value);
            } else {
                castValue = value.toString();
            }
        } else {
            castValue = field.getType().cast(value);
        }

        m.invoke(data, castValue);

        return castValue;
    }

    private String getSetterMethodNameForField(Field field) {
        return "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
    }

}