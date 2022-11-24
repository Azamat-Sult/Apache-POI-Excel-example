package com.example.ApachePOIExcelExample.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Type2 implements EnumWithDescription {

    TYPE_2_1 ("type_2_1_desc"),
    TYPE_2_2 ("type_2_2_desc"),
    TYPE_2_3 ("type_2_3_desc");

    private final String description;

    public static Type2 getByDescription(String description) {
        return Arrays.stream(values())
                .filter(type1 -> type1.getDescription().equalsIgnoreCase(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "Type1 с description \"%s\" не найден", description)));
    }

}