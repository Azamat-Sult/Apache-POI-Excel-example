package com.example.ApachePOIExcelExample.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private List<String> errors;

    public ApiError(String error) {
        this.errors = Collections.singletonList(error);
    }

}