package com.example.ApachePOIExcelExample.exception;

public class ValueNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3343445455245L;

    public ValueNotFoundException(String msg) {
        super(msg);
    }

}