package com.example.ApachePOIExcelExample.scheduler.model;

import com.example.ApachePOIExcelExample.scheduler.enums.JobResult;
import com.example.ApachePOIExcelExample.scheduler.enums.JobStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ScheduledJobDto {

    private Long id;
    private String jobCode;
    private Instant lastStartTime;
    private Instant lastFinishTime;
    private Instant lastSuccessStartTime;
    private JobResult jobResult;
    private JobStatus jobStatus;
    private String jobSchedule;
    private boolean isActive;
    private String title;
    private String parameters;

}