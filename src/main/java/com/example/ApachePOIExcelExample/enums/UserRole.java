package com.example.ApachePOIExcelExample.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    ROLE_USER("Пользователь"),
    ROLE_ADMIN("Администратор"),
    ROLE_OWNER("Владелец ресурса");

    private final String description;

}