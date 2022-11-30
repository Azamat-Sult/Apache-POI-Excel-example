package com.example.ApachePOIExcelExample.scheduler.model;

import com.example.ApachePOIExcelExample.scheduler.enums.JobResult;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ScheduledJobLogDto {

    private Long id;
    private String jobCode;
    private String logCode;
    private Instant startTime;
    private Instant finishTime;
    private JobResult jobResult;

}