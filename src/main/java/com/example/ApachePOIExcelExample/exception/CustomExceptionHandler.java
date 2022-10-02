package com.example.ApachePOIExcelExample.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private String shortenedStackTrace(Exception e) {
        var maxLines = 16;
        var writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        var lines = writer.toString().split("\n");
        return Arrays.stream(lines)
                .limit(maxLines)
                .map(line -> line + "\n")
                .collect(Collectors.joining());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> internalServerError(Exception e, WebRequest request) {
        log.error(shortenedStackTrace(e));
        var apiException = new ApiError("Internal server error: contact admin");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiException);
    }

    @ExceptionHandler(ReportException.class)
    public final ResponseEntity<Object> reportError(Exception e, WebRequest request) {
        log.error(shortenedStackTrace(e));
        var apiException = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException);
    }

    @ExceptionHandler(ParsingException.class)
    public final ResponseEntity<Object> parsingError(Exception e, WebRequest request) {
        log.error(shortenedStackTrace(e));
        var apiException = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException);
    }

}