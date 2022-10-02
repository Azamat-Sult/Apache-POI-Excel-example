package com.example.ApachePOIExcelExample.model;

import com.example.ApachePOIExcelExample.entity.RoleEntity;
import com.example.ApachePOIExcelExample.enums.UserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(description = "Роль пользователя")
public class RoleDto {

    public RoleDto(UserRole userRole, String description) {
        this.userRole = userRole;
        this.description = description;
    }

    @ApiModelProperty(value = "Идентификатор", example = "3", required = true)
    private Long id;
    @ApiModelProperty(value = "Роль", example = "USER", required = true)
    private UserRole userRole;
    @ApiModelProperty(value = "Описание роли", example = "Пользователь", required = true)
    private String description;

    public RoleEntity toEntity() {
        return RoleEntity.builder()
                .id(this.id)
                .userRole(this.userRole)
                .description(this.description)
                .build();
    }

}