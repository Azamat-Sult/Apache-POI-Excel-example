package com.example.ApachePOIExcelExample.service;

import com.example.ApachePOIExcelExample.annotation.ReportFieldControl.AccessType;
import com.example.ApachePOIExcelExample.entity.SomeEntity;
import com.example.ApachePOIExcelExample.model.ReportFieldsDto;
import com.example.ApachePOIExcelExample.model.ReportSheetDto;
import com.example.ApachePOIExcelExample.util.ExcelUtil;
import com.example.ApachePOIExcelExample.util.ReportTemplateUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final SomeEntityService someEntityService;

    public ReportService(SomeEntityService someEntityService) {
        this.someEntityService = someEntityService;
    }

    public InputStreamResource buildReport(List<String> fields) {

        compileReportColumns(fields);

        var report = someEntityService.findAll().stream()
                .map(SomeEntity::toReportSheetDto)
                .toList();

        XSSFWorkbook workbook = new XSSFWorkbook();

        var reportSheet = new ExcelReport<>(report, ReportSheetDto.class, fields);
        workbook = reportSheet.buildSingleSheetReport(workbook, true);

        return ExcelUtil.buildExcel(workbook);
    }

    private void compileReportColumns(List<String> fieldsList) {
        // Если пользователь не выбрал поля - то в отчете будут все разрешенные к показу
        if (fieldsList.isEmpty()) {
            fieldsList.addAll(
                    getFieldsByAccessType(AccessType.IF_ASKED).stream()
                            .map(ReportFieldsDto::getId)
                            .toList()
            );
        }
        // Обязательные поля
        fieldsList.addAll(
                getFieldsByAccessType(AccessType.SHOW_ALWAYS).stream()
                        .map(ReportFieldsDto::getId)
                        .toList()
        );
        // Связанные с уже выбранными поля
        fieldsList.addAll(getLinkedFields(fieldsList));
    }

    public List<ReportFieldsDto> getFieldsByAccessType(AccessType accessType) {
        return ReportTemplateUtil.getFieldsByAccessType(ReportSheetDto.class, accessType);
    }

    private List<String> getLinkedFields(List<String> fieldsList) {
        return ReportTemplateUtil.getLinkedFields(ReportSheetDto.class, fieldsList);
    }

}