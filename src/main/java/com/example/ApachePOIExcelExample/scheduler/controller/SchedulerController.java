package com.example.ApachePOIExcelExample.scheduler.controller;

import com.example.ApachePOIExcelExample.scheduler.model.CronParameter;
import com.example.ApachePOIExcelExample.scheduler.model.ScheduledJobDto;
import com.example.ApachePOIExcelExample.scheduler.model.ScheduledJobLogDto;
import com.example.ApachePOIExcelExample.scheduler.service.ScheduledJobLogService;
import com.example.ApachePOIExcelExample.scheduler.service.SchedulerService;
import com.example.ApachePOIExcelExample.util.ReportTemplateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scheduler")
@Api(tags = "SchedulerController")
public class SchedulerController {

    private final SchedulerService schedulerService;
    private final ScheduledJobLogService scheduledJobLogService;

    @GetMapping("/job/list")
    @ApiOperation(value = "Get all scheduled jobs")
    public ResponseEntity<List<ScheduledJobDto>> getAllScheduledJobs() {
        return ResponseEntity.ok().body(schedulerService.getAllScheduledJobs());
    }

    @GetMapping("/job/{jobCode}")
    @ApiOperation(value = "Get scheduled job by jobCode")
    public ResponseEntity<ScheduledJobDto> getScheduledJob(@PathVariable String jobCode) {
        return ResponseEntity.ok().body(schedulerService.getScheduledJob(jobCode));
    }

    @PostMapping("/job/start/{jobCode}")
    @ApiOperation(value = "Start scheduled job")
    public ResponseEntity<ScheduledJobDto> startScheduledJob(@PathVariable String jobCode) {
        return ResponseEntity.ok().body(schedulerService.startScheduledJob(jobCode));
    }

    @PostMapping("/job/start/{jobCode}/once")
    @ApiOperation(value = "Start scheduled job once")
    public ResponseEntity<ScheduledJobDto> startScheduledJobOnce(@PathVariable String jobCode) {
        return ResponseEntity.ok().body(schedulerService.startScheduledJobOnce(jobCode));
    }

    @PutMapping("/schedule/{jobCode}")
    @ApiOperation(value = "Change schedule of job")
    public ResponseEntity<ScheduledJobDto> changeSchedule(@PathVariable String jobCode,
                                                              @RequestBody CronParameter schedule) {
        return ResponseEntity.ok().body(schedulerService.changeSchedule(jobCode, schedule.getCron()));
    }

    @PostMapping("/job/stop/{jobCode}")
    @ApiOperation(value = "Stop scheduled job")
    public ResponseEntity<ScheduledJobDto> stopScheduledJob(@PathVariable String jobCode) {
        return ResponseEntity.ok().body(schedulerService.stopScheduledJob(jobCode));
    }

    @PutMapping("/parameters/{jobCode}")
    @ApiOperation(value = "Change parameters of job")
    public ResponseEntity<ScheduledJobDto> changeParameters(@PathVariable String jobCode,
                                                          @RequestBody String parameters) {
        return ResponseEntity.ok().body(schedulerService.changeParameters(jobCode, parameters));
    }

    @GetMapping("/log/list")
    @ApiOperation(value = "Get all scheduled jobs logs")
    public ResponseEntity<List<ScheduledJobLogDto>> getAllScheduledJobLogs() {
        return ResponseEntity.ok().body(scheduledJobLogService.getAllScheduledJobLogs());
    }

    @GetMapping("/job/{jobCode}/log/{logCode}")
    @ApiOperation(value = "Get scheduled job logs by jobCode and logCode")
    public ResponseEntity<Resource> getScheduledJobLogs(@PathVariable String jobCode,
                                                        @PathVariable String logCode) {
        var contentType = "application/vnd.ms-excel; charset=utf-8";
        var resourceStream = schedulerService.getLogContent(jobCode, logCode);
        return ReportTemplateUtil.response(resourceStream, jobCode + "_log.xlsx", contentType);
    }

}