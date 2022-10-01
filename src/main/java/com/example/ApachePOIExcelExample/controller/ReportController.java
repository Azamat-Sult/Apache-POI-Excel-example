package com.example.ApachePOIExcelExample.controller;

import com.example.ApachePOIExcelExample.exception.ReportException;
import com.example.ApachePOIExcelExample.model.ReportRequest;
import com.example.ApachePOIExcelExample.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "ReportController")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/report")
    @ApiOperation(value = "download xlsx file template")
    public ResponseEntity<Resource> getReport(@RequestBody ReportRequest request) throws ReportException {
        var contentDisposition = "attachment; filename=report.xlsx";
        var headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        headers.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        headers.set(HttpHeaders.CONTENT_TYPE, "application/vnd.ms-excel; charset=utf-8");
        var resourceStream = reportService.buildReport(request.getFields());
        return new ResponseEntity<>(resourceStream, headers, HttpStatus.OK);
    }

}