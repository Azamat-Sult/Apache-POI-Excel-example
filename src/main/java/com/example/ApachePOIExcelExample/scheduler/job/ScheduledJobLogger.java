package com.example.ApachePOIExcelExample.scheduler.job;

import org.springframework.core.io.InputStreamResource;

public interface ScheduledJobLogger {

    InputStreamResource getLogContent(final String jobCode, String logCode);

}