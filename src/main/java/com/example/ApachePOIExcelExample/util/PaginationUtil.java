package com.example.ApachePOIExcelExample.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

public class PaginationUtil {

    private PaginationUtil() {}

    public static HttpHeaders generatePaginationHttpHeaders(Page page) {
        var headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        var lastPage = 0;
        if (page.getTotalPages() > 0) {
            lastPage = page.getTotalPages() - 1;
        }
        headers.add("X-Total-Page", Long.toString(lastPage + 1));
        return headers;
    }

}