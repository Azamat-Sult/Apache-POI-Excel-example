package com.example.ApachePOIExcelExample.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogMessage {

    CONTROLLER_CALL("[%s] %s args = [%s]");

    private final String message;

}