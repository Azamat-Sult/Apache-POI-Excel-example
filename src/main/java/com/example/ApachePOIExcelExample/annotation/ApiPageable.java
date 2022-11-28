package com.example.ApachePOIExcelExample.annotation;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataTypeClass = Integer.class, paramType = "query", defaultValue = "0", value = "page number"),
        @ApiImplicitParam(name = "size", dataTypeClass = Integer.class, paramType = "query", defaultValue = "10", value = "page size"),
        @ApiImplicitParam(name = "sort", dataTypeClass = String.class, allowMultiple = true, paramType = "query", value = "asc|desc, multiple allowed")
})
public @interface ApiPageable {

}