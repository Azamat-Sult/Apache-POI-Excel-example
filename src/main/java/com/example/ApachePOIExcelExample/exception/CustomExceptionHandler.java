package com.example.ApachePOIExcelExample.exception;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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

    @SneakyThrows
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        if (ex.getConstraintViolations() == null) {
            var apiException = new ApiError(ex.getMessage());
            return ResponseEntity.badRequest().body(apiException);
        }
        var scv = ex.getConstraintViolations();
        scv.removeIf(cv -> cv.getMessage().isEmpty());
        var iterator = scv.iterator();
        Set<String> errors = new HashSet<>();
        while (iterator.hasNext()) {
            var next = iterator.next();
            if (!next.getMessage().isEmpty()) {
                var error = next.getPropertyPath().toString()
                        .replaceAll("create.dto.", "")
                        .replaceAll("update.dto.", "")
                        .replaceAll("delete.dto.", "");
                errors.add(error + ": " + next.getMessage());
            }
        }
        var apiException = new ApiError(new ArrayList<>(errors));
        return ResponseEntity.badRequest().body(apiException);
    }

}