package com.example.ApachePOIExcelExample.repository;

import com.example.ApachePOIExcelExample.entity.ArrayColumnExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArrayColumnExampleRepository extends JpaRepository<ArrayColumnExample, Long> {
}