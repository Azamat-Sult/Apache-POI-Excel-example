package com.example.ApachePOIExcelExample.controller;

import com.example.ApachePOIExcelExample.service.FileUploaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ExcelUploaderController {

    private final FileUploaderService fileUploaderService;

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadExcel(@RequestParam(name = "filename") MultipartFile file) {
        fileUploaderService.uploadFile(file);
        return ResponseEntity.ok().build();
    }

}