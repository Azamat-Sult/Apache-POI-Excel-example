package com.example.ApachePOIExcelExample.scheduler.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobResult {

    SUCCESS ("SUCCESS"),
    FAILED ("FAILED"),
    FINISHED_WITH_ERRORS ("FINISHED_WITH_ERRORS");

    private final String result;

}