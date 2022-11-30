package com.example.ApachePOIExcelExample.scheduler.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobStatus {

    RUNNING ("RUNNING"),
    STOPPED ("STOPPED"),
    FINISHED ("FINISHED");

    private final String status;

}