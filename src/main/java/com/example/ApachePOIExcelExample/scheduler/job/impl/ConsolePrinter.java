package com.example.ApachePOIExcelExample.scheduler.job.impl;

import com.example.ApachePOIExcelExample.scheduler.entity.ScheduledJobEntity;
import com.example.ApachePOIExcelExample.scheduler.job.AbstractScheduledJob;
import com.example.ApachePOIExcelExample.scheduler.job.params.ConsolePrinterParameters;
import com.example.ApachePOIExcelExample.scheduler.service.ScheduledJobLogService;
import com.example.ApachePOIExcelExample.scheduler.service.ScheduledJobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;

import static com.example.ApachePOIExcelExample.exception.SchedulerException.JOB_PARAMETERS_WRONG_FORMAT;

@Component
public class ConsolePrinter extends AbstractScheduledJob {

    private final ObjectMapper objectMapper;
    private ConsolePrinterParameters parameters;

    public ConsolePrinter(ScheduledJobService scheduledJobService, ScheduledJobLogService loggingService,
                          PlatformTransactionManager transactionManager, ObjectMapper objectMapper) {
        super(scheduledJobService, loggingService, transactionManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean perform(ScheduledJobEntity job, String logCode) {
        var success = false;
        for (int i = 0; i < parameters.getTimes(); i++) {
            System.out.println("============== HELLO ==============");
            success = true;
        }
        return success;
    }

    @Override
    public String parseParameters(String parameters) throws IOException {
        try {
            this.parameters = objectMapper.readValue(parameters, ConsolePrinterParameters.class);
        } catch (Exception ex) {
            throw new IOException(JOB_PARAMETERS_WRONG_FORMAT);
        }
        return objectMapper.writeValueAsString(this.parameters);
    }

    @Override
    public void run() {
        super.run("consolePrinter", false);
    }

}