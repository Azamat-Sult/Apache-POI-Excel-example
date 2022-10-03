package com.example.ApachePOIExcelExample.entity;

import com.example.ApachePOIExcelExample.enums.UserRole;
import com.example.ApachePOIExcelExample.model.RoleDto;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ROLES")
@EqualsAndHashCode(of = "userRole")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "ROLE", unique = true, nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "DESCRIPTION", nullable = false, length = 64)
    private String description;

    public RoleDto toDto() {
        return RoleDto.builder()
                .id(this.id)
                .userRole(this.userRole)
                .description(this.description)
                .build();
    }

}