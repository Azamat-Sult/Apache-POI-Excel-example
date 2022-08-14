package com.example.ApachePOIExcelExample.exception;

public class ParsingException extends RuntimeException {

    public ParsingException(String msg, Object... args) {
        super(String.format("Ошибка обработки файла. %s", String.format(msg, args)));
    }

    public ParsingException(Exception e) {
        super(String.format("Ошибка обработки файла. %s", e.getMessage()));
    }

}