package com.example.ApachePOIExcelExample.repository;

import com.example.ApachePOIExcelExample.entity.SomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SomeEntityRepository extends JpaRepository<SomeEntity, Long> {
}