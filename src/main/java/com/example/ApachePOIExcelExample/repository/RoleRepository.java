package com.example.ApachePOIExcelExample.repository;

import com.example.ApachePOIExcelExample.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}