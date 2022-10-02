package com.example.ApachePOIExcelExample.service;

import com.example.ApachePOIExcelExample.enums.UserRole;
import com.example.ApachePOIExcelExample.model.RoleDto;
import com.example.ApachePOIExcelExample.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public void initRoles() {
        var roles = Arrays.stream(UserRole.values())
                .map(role -> new RoleDto(role, role.getDescription()))
                .map(RoleDto::toEntity)
                .toList();
        roleRepository.saveAll(roles);
    }

}