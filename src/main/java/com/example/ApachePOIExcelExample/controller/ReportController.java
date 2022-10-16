package com.example.ApachePOIExcelExample.controller;

import com.example.ApachePOIExcelExample.exception.ReportException;
import com.example.ApachePOIExcelExample.model.ReportRequest;
import com.example.ApachePOIExcelExample.model.ReportSheetDto;
import com.example.ApachePOIExcelExample.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "ReportController")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/get_report")
    public ModelAndView getReportPage() {
        ModelAndView mav = new ModelAndView("get_report");
        List<String> displayFields = Arrays.stream(ReportSheetDto.class.getDeclaredFields())
                .map(Field::getName)
                .toList();
        mav.addObject("fields", displayFields);
        mav.addObject("ReportRequest", new ReportRequest());
        return mav;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN','ROLE_OWNER')")
    @PostMapping("/report")
    @ApiOperation(value = "download xlsx report file")
    public ResponseEntity<Resource> getReport(@ModelAttribute ReportRequest request) throws ReportException {
        var contentDisposition = "attachment; filename=report.xlsx";
        var headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        headers.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        headers.set(HttpHeaders.CONTENT_TYPE, "application/vnd.ms-excel; charset=utf-8");
        var resourceStream = reportService.buildReport(request.getFields());
        return new ResponseEntity<>(resourceStream, headers, HttpStatus.OK);
    }

}