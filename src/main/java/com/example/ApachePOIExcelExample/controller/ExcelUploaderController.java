package com.example.ApachePOIExcelExample.controller;

import com.example.ApachePOIExcelExample.service.FileUploaderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Api(tags = "ExcelUploaderController")
public class ExcelUploaderController {

    private final FileUploaderService fileUploaderService;

    @PostMapping("/upload")
    @ApiOperation(value = "upload xlsx file")
    public ResponseEntity<Void> uploadExcel(@RequestPart(name = "filename") MultipartFile file) {
        fileUploaderService.uploadFile(file);
        return ResponseEntity.ok().build();
    }

}