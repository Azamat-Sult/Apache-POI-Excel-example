package com.example.ApachePOIExcelExample.repository;

import com.example.ApachePOIExcelExample.entity.RoleEntity;
import com.example.ApachePOIExcelExample.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByUserRole(UserRole userRole);

}