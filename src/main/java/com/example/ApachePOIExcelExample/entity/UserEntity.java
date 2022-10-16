package com.example.ApachePOIExcelExample.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
@EqualsAndHashCode(of = "id")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "EMAIL", unique = true, nullable = false, length = 32)
    private String email;

    @Column(name = "PASSWORD", nullable = false, length = 32)
    private String password;

    @Column(name = "FULL_NAME", nullable = false, length = 64)
    private String fullName;

    @Column(name = "NOT_EXPIRED", nullable = false)
    private boolean notExpired;

    @Column(name = "NOT_LOCKED", nullable = false)
    private boolean notLocked;

    @Column(name = "CREDENTIALS_NOT_EXPIRED", nullable = false)
    private boolean credentialsNotExpired;

    @Column(name = "IS_ENABLED", nullable = false)
    private boolean isEnabled;

    @Column(name = "LAST_LOGIN")
    private Instant lastLogin;

    @Column(name = "REGISTERED")
    private Instant registered;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "USERS_ROLES",
            joinColumns = {@JoinColumn(name = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")}
    )
    private Set<RoleEntity> roles;

}