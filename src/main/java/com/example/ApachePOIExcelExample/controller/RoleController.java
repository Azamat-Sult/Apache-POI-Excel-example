package com.example.ApachePOIExcelExample.controller;

import com.example.ApachePOIExcelExample.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "RoleController")
public class RoleController {

    private final RoleService roleService;

    @PutMapping("/init_roles")
    @ApiOperation(value = "init roles in DB")
    public ResponseEntity<Void> initRoles() {
        roleService.initRoles();
        return ResponseEntity.ok().build();
    }

}