package com.example.ApachePOIExcelExample.scheduler.service;

import com.example.ApachePOIExcelExample.scheduler.entity.ScheduledJobEntity;
import com.example.ApachePOIExcelExample.scheduler.repository.ScheduledJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledJobService {

    private final ScheduledJobRepository scheduledJobRepository;

    public List<ScheduledJobEntity> getAllScheduledJobs() {
        return scheduledJobRepository.findAll(Sort.by("id"));
    }

    public ScheduledJobEntity getScheduledJobByCode(String jobCode) {
        return scheduledJobRepository.findScheduledJobEntityByJobCode(jobCode).orElse(null);
    }

    public void saveJobState(ScheduledJobEntity job) {
        scheduledJobRepository.save(job);
    }

}