package com.example.ApachePOIExcelExample.controller;

import com.example.ApachePOIExcelExample.service.FileUploaderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@Api(tags = "ExcelUploaderController")
public class ExcelUploaderController {

    private final FileUploaderService fileUploaderService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_OWNER')")
    @PostMapping("/upload")
    @ApiOperation(value = "upload xlsx file")
    public String uploadExcel(@RequestPart(name = "file") MultipartFile file) {
        fileUploaderService.uploadFile(file);
        return "redirect:/get_report";
    }

}