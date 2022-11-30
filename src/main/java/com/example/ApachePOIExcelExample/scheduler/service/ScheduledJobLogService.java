package com.example.ApachePOIExcelExample.scheduler.service;

import com.example.ApachePOIExcelExample.scheduler.entity.ScheduledJobLogEntity;
import com.example.ApachePOIExcelExample.scheduler.enums.JobResult;
import com.example.ApachePOIExcelExample.scheduler.model.ScheduledJobLogDto;
import com.example.ApachePOIExcelExample.scheduler.repository.ScheduledJobLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ScheduledJobLogService {

    private final ScheduledJobLogRepository logRepository;

    public void start(String jobCode, String logCode, Instant startTime) {
        var startLog = ScheduledJobLogEntity.builder()
                .jobCode(jobCode)
                .logCode(logCode)
                .startTime(startTime)
                .build();
        logRepository.save(startLog);
    }

    public void finish(String logCode, Instant finishTime, JobResult jobResult) {
        var finishLog = logRepository.findByLogCode(logCode)
                .orElseThrow(EntityNotFoundException::new);
        finishLog.setFinishTime(finishTime);
        finishLog.setJobResult(jobResult);
        logRepository.save(finishLog);
    }

    public void error(String logCode, Instant finishTime) {
        var errorLog = logRepository.findByLogCode(logCode)
                .orElseThrow(EntityNotFoundException::new);
        errorLog.setFinishTime(finishTime);
        errorLog.setJobResult(JobResult.FAILED);
        logRepository.save(errorLog);
    }

    public List<ScheduledJobLogDto> getAllScheduledJobLogs() {
        return logRepository.findAll().stream()
                .map(ScheduledJobLogEntity::toDto)
                .collect(Collectors.toList());
    }

    public List<ScheduledJobLogDto> getScheduledJobLogs(String jobCode) {
        return logRepository.findByJobCode(jobCode).stream()
                .map(ScheduledJobLogEntity::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ScheduledJobLogDto> getScheduledJobLastLog(String jobCode) {
        var request = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id")));
        return logRepository.findByJobCode(jobCode, request)
                .map(ScheduledJobLogEntity::toDto).stream()
                .findFirst();
    }

}