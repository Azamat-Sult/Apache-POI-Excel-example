package com.example.ApachePOIExcelExample.controller;

import com.example.ApachePOIExcelExample.model.SomeDataLoadDto;
import com.example.ApachePOIExcelExample.service.FileUploaderService;
import com.example.ApachePOIExcelExample.service.SomeEntityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Validated
@Controller
@RequiredArgsConstructor
@Api(tags = "ExcelUploaderController")
public class ExcelUploaderController {

    private final FileUploaderService fileUploaderService;
    private final SomeEntityService someEntityService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_OWNER')")
    @PostMapping("/upload")
    @ApiOperation(value = "upload xlsx file")
    public String uploadExcel(@RequestPart(name = "file") MultipartFile file) {
        fileUploaderService.uploadFile(file);
        return "redirect:/get_report";
    }

    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_OWNER')")
    @PostMapping("/create")
    @ApiOperation(value = "create new record")
    public String create(@Valid @RequestBody SomeDataLoadDto dto, Errors errors) {
        someEntityService.create(dto);
        return "redirect:/get_report";
    }

}