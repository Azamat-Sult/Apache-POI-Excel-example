package com.example.ApachePOIExcelExample.controller;

import com.example.ApachePOIExcelExample.annotation.ApiPageable;
import com.example.ApachePOIExcelExample.entity.ArrayColumnExample;
import com.example.ApachePOIExcelExample.entity.UserEntity;
import com.example.ApachePOIExcelExample.exception.ReportException;
import com.example.ApachePOIExcelExample.model.ReportRequest;
import com.example.ApachePOIExcelExample.model.ReportSheetDto;
import com.example.ApachePOIExcelExample.service.ArrayColumnExampleService;
import com.example.ApachePOIExcelExample.service.ReportService;
import com.example.ApachePOIExcelExample.service.SomeEntityService;
import com.example.ApachePOIExcelExample.service.UserService;
import com.example.ApachePOIExcelExample.util.ReportTemplateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Api(tags = "ReportController")
public class ReportController {

    private final ReportService reportService;
    private final SomeEntityService someEntityService;
    private final ArrayColumnExampleService arrayColumnExampleService;
    private final UserService userService;

    @GetMapping("/get_report")
    public ModelAndView getReportPage() {
        ModelAndView mav = new ModelAndView("get_report");
        List<String> displayFields = Arrays.stream(ReportSheetDto.class.getDeclaredFields())
                .map(Field::getName)
                .toList();
        List<ReportSheetDto> data = someEntityService.findAll();
        mav.addObject("fields", displayFields);
        mav.addObject("ReportRequest", new ReportRequest());
        mav.addObject("ReportData", data);
        mav.addObject("Row", new ReportSheetDto());
        return mav;
    }

    @ApiPageable
    @GetMapping("/get_rows")
    @ApiOperation(value = "Get rows in pages")
    public ResponseEntity<List<ReportSheetDto>> getRowsInPages(@ApiIgnore @PageableDefault(
            sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return someEntityService.getRowsInPages(pageable);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN','ROLE_OWNER')")
    @PostMapping("/report")
    @ApiOperation(value = "download xlsx report file")
    public ResponseEntity<Resource> getReport(@ModelAttribute ReportRequest request) throws ReportException {
        var contentType = "application/vnd.ms-excel; charset=utf-8";
        var resourceStream = reportService.buildReport(request.getFields());
        return ReportTemplateUtil.response(resourceStream, "report.xlsx", contentType);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER')")
    @GetMapping("/delete/{id}")
    @ApiOperation(value = "Delete row")
    public String deleteRow(@PathVariable("id") Long id) {
        someEntityService.deleteById(id);
        return "redirect:/get_report";
    }

    @GetMapping("/array_column_example/print")
    public ResponseEntity<Void> addTwoAndPrintAllEntities() {
        arrayColumnExampleService.addTwoAndPrintAllEntities();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/array_column_example/array")
    public ResponseEntity<List<ArrayColumnExample>> findByArrayColumnContainingIgnoreCase(
            @RequestParam(name = "search", required = false) String search) {
        return ResponseEntity.ok().body(arrayColumnExampleService.findByArrayColumnContainingIgnoreCase(search));
    }

    @GetMapping("/array_column_example/object")
    public ResponseEntity<String> findByObjectContainingIgnoreCase(
            @RequestParam(name = "search", required = false) String search) {
        return ResponseEntity.ok().body(arrayColumnExampleService.someStringListContainingText(search));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers(@RequestParam(name = "search", required = false) String search) {
        return ResponseEntity.ok().body(userService.findUsers(search));
    }

}