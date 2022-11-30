package com.example.ApachePOIExcelExample.scheduler.job.logger;

import com.example.ApachePOIExcelExample.scheduler.job.ScheduledJobLogger;
import com.example.ApachePOIExcelExample.scheduler.model.ScheduledJobLogDto;
import com.example.ApachePOIExcelExample.scheduler.service.ScheduledJobLogService;
import com.example.ApachePOIExcelExample.service.ExcelReport;
import com.example.ApachePOIExcelExample.util.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsolePrinterLogger implements ScheduledJobLogger {

    private final ScheduledJobLogService scheduledJobLogService;

    @Override
    public InputStreamResource getLogContent(String jobCode, String logCode) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        var log = scheduledJobLogService.getScheduledJobLogs(jobCode);
        var logSheet = new ExcelReport<>(log, ScheduledJobLogDto.class);
        workbook = logSheet.buildSingleSheetReport(workbook, true);
        return ExcelUtil.buildExcel(workbook);
    }

}