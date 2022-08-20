package com.example.ApachePOIExcelExample.service;

import com.example.ApachePOIExcelExample.annotation.ReportTemplate;
import com.example.ApachePOIExcelExample.enums.EnumWithDescription;
import com.example.ApachePOIExcelExample.exception.ReportException;
import com.example.ApachePOIExcelExample.util.DateUtils;
import com.example.ApachePOIExcelExample.util.ExcelUtil;
import com.example.ApachePOIExcelExample.util.ReportTemplateUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class ExcelReport<E> {

    private final List<E> reportData;
    private final Class<E> template;
    private final Field[] fields;

    private CellStyle headerStyle;
    private CellStyle labelStyle;
    private CellStyle numberStyle;
    private CellStyle dateStyle;

    public ExcelReport(List<E> reportData, Class<E> template, List<String> fields) {
        this.reportData = reportData;
        this.template = template;
        this.fields = ReportTemplateUtil.filterSelectedFields(template, fields);
    }

    public XSSFWorkbook buildSingleSheetReport(XSSFWorkbook workbook, boolean setSecretStamp) throws ReportException {
        try {
            createStyles(workbook);
            var worksheet = createWorkSheet(workbook);
            createHeader(worksheet);
            fillReport(worksheet);
            createFooter(worksheet);
            if (setSecretStamp) {
                ExcelUtil.addRunningTitle(worksheet);
            }
            return workbook;
        } catch (Exception e) {
            throw new ReportException(e);
        }
    }

    private void createStyles(XSSFWorkbook workbook) {
        headerStyle = ExcelUtil.createHeaderStyle(workbook);
        labelStyle = ExcelUtil.createLabelStyle(workbook);
        numberStyle = ExcelUtil.createNumberStyle(workbook);
        dateStyle = ExcelUtil.createDateStyle(workbook);
    }

    private XSSFSheet createWorkSheet(XSSFWorkbook workbook) {
        var reportTemplate = template.getAnnotation(ReportTemplate.class);
        String reportName;
        if (Objects.isNull(reportTemplate)) {
            reportName = template.getSimpleName();
        } else {
            reportName = reportTemplate.name();
        }
        return workbook.createSheet(reportName);
    }

    private void createHeader(XSSFSheet sheet) {
        var xTopRow = sheet.createRow(0);
        int column = 0;
        for (var field : fields) {
            var label = ReportTemplateUtil.getFieldName(field);
            createCell(xTopRow, label, headerStyle, column);
            column++;
        }
    }

    private void createCell(XSSFRow xRow, Object value, CellStyle style, int column) {
        var xCell = xRow.createCell(column);
        if (Objects.isNull(value)) {
            xCell.setCellValue("");
        } else {
            if (value instanceof EnumWithDescription) {
                xCell.setCellValue(((EnumWithDescription) value).getDescription());
            } else if (value instanceof String) {
                xCell.setCellValue(value.toString());
            } else if (value instanceof BigDecimal) {
                xCell.setCellValue(((BigDecimal) value).doubleValue());
            } else if (value instanceof Instant) {
                xCell.setCellValue(DateUtils.formatDate((Instant) value));
            } else {
                xCell.setCellValue(value.toString());
            }
        }
        xCell.setCellStyle(style);
    }

    private void fillReport(XSSFSheet sheet)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        int row = 1;
        for (var data : reportData) {
            var xRow = sheet.createRow(row);
            int column = 0;
            for (var field : fields) {
                var fieldType = field.getType().getSimpleName();
                Object value;
                if (Modifier.isPublic(field.getModifiers())) {
                    value = field.get(data);
                } else {
                    value = getFieldValue(data, field);
                }
                CellStyle style = switch (fieldType) {
                    case "BigDecimal" -> numberStyle;
                    case "LocalDateTime" -> dateStyle;
                    default -> labelStyle;
                };
                createCell(xRow, value, style, column);
                column++;
            }
            row++;
        }
    }

    private Object getFieldValue(E data, Field field)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var methodName = getGetterMethodNameForField(data, field);
        var m = data.getClass().getDeclaredMethod(methodName);
        return m.invoke(data);
    }

    private String getGetterMethodNameForField(Object obj, Field field) {
        if (field.getType() == Boolean.class || field.getType() == boolean.class) {
            String isGetterName = "is"
                    + field.getName().substring(0, 1).toUpperCase()
                    + field.getName().substring(1);
            try {
                obj.getClass().getDeclaredMethod(isGetterName);
                return isGetterName;
            } catch (NoSuchMethodException ignored) { }
        }
        return "get" + field.getName().substring(0, 1).toUpperCase()
                + field.getName().substring(1);
    }

    private void createFooter(XSSFSheet sheet) {
        IntStream.range(0, fields.length).forEach(sheet::autoSizeColumn);
    }

}