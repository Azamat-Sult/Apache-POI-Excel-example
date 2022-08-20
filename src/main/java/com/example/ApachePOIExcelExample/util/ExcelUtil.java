package com.example.ApachePOIExcelExample.util;

import com.example.ApachePOIExcelExample.exception.ReportException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class ExcelUtil {

    public static InputStreamResource buildExcel(XSSFWorkbook workbook) throws ReportException {
        try {
            var outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            var inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            return new InputStreamResource(Objects.requireNonNull(inputStream));
        } catch (Exception e) {
            throw new ReportException(e);
        }
    }

    public static CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        var cellStyle = createDefaultStyle(workbook);
        var fontBold = workbook.createFont();
        fontBold.setBold(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(fontBold);
        return cellStyle;
    }

    public static CellStyle createDefaultStyle(XSSFWorkbook workbook) {
        var cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return cellStyle;
    }

    public static CellStyle createLabelStyle(XSSFWorkbook workbook) {
        var cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        return cellStyle;
    }

    public static CellStyle createNumberStyle(XSSFWorkbook workbook) {
        var cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###0.00"));
        return cellStyle;
    }

    public static CellStyle createDateStyle(XSSFWorkbook workbook) {
        var cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        return cellStyle;
    }

    public static void addRunningTitle(XSSFSheet sheet) {
        Header header = sheet.getHeader();
        header.setRight("TOP SECRET");
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setHResolution((short) 1);
        printSetup.setLandscape(true);
        printSetup.setPaperSize(PrintSetup.A4_PAPERSIZE);
        sheet.getFooter().setRight("page " + HeaderFooter.page() + " from " + HeaderFooter.numPages());
    }

}