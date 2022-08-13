package com.example.ApachePOIExcelExample.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Type1 {

    TYPE_1_1 ("type_1_1_desc"),
    TYPE_1_2 ("type_1_2_desc"),
    TYPE_1_3 ("type_1_3_desc");

    private final String description;

}