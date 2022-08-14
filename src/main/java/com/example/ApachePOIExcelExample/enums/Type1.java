package com.example.ApachePOIExcelExample.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Type1 implements EnumWithDescription {

    TYPE_1_1 ("type_1_1_desc"),
    TYPE_1_2 ("type_1_2_desc"),
    TYPE_1_3 ("type_1_3_desc");

    private final String description;

    public static Type1 getByDescription(String description) {
        return Arrays.stream(values())
                .filter(type1 -> type1.getDescription().equalsIgnoreCase(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "Type1 с description \"%s\" не найден", description)));
    }

}