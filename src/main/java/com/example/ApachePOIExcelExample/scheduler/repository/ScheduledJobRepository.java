package com.example.ApachePOIExcelExample.scheduler.repository;

import com.example.ApachePOIExcelExample.scheduler.entity.ScheduledJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduledJobRepository extends JpaRepository<ScheduledJobEntity, Long> {
    Optional<ScheduledJobEntity> findScheduledJobEntityByJobCode(String jobCode);

}