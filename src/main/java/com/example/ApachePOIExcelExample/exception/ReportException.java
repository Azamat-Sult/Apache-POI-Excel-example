package com.example.ApachePOIExcelExample.exception;

public class ReportException extends RuntimeException {

    public ReportException(String msg, Object... args) {
        super(String.format("Ошибка формирования отчета. %s", String.format(msg, args)));
    }

    public ReportException(Exception e) {
        super(String.format("Ошибка формирования отчета. %s", e.getMessage()));
    }

}