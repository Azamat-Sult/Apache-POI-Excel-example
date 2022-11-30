package com.example.ApachePOIExcelExample.scheduler.repository;

import com.example.ApachePOIExcelExample.scheduler.entity.ScheduledJobLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduledJobLogRepository extends JpaRepository<ScheduledJobLogEntity, Long> {

    Optional<ScheduledJobLogEntity> findByLogCode(String logCode);

    Page<ScheduledJobLogEntity> findByJobCode(String jobCode, Pageable pageable);

    List<ScheduledJobLogEntity> findByJobCode(String jobCode);

}