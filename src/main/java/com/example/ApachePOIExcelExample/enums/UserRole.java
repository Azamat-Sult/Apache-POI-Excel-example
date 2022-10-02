package com.example.ApachePOIExcelExample.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    USER("Пользователь"),
    ADMIN("Администратор"),
    OWNER("Владелец ресурса");

    private final String description;

}