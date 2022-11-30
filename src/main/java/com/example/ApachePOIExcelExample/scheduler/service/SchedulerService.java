package com.example.ApachePOIExcelExample.scheduler.service;

import com.example.ApachePOIExcelExample.exception.SchedulerException;
import com.example.ApachePOIExcelExample.scheduler.job.ScheduledJobLogger;
import com.example.ApachePOIExcelExample.scheduler.entity.ScheduledJobEntity;
import com.example.ApachePOIExcelExample.scheduler.enums.JobStatus;
import com.example.ApachePOIExcelExample.scheduler.job.ScheduledJob;
import com.example.ApachePOIExcelExample.scheduler.model.ScheduledJobDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.InputStreamResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import static com.example.ApachePOIExcelExample.exception.SchedulerException.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final ScheduledJobService scheduledJobService;
    private final ScheduledJobLogService scheduledJobLogService;
    private final ThreadPoolTaskScheduler poolTaskScheduler;
    private final Map<String, ScheduledJob> jobThreads;
    private final Map<String, ScheduledJobLogger> jobLoggers;
    private final Map<Runnable, ScheduledFuture<?>> listenableFutureMap = new ConcurrentHashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void initScheduledJobs() {
        var jobs = scheduledJobService.getAllScheduledJobs();
        jobs.forEach(job -> {
            if (JobStatus.RUNNING.equals(job.getJobStatus())) {
                job.setJobStatus(JobStatus.STOPPED);
                scheduledJobService.saveJobState(job);
            }
            if (job.isActive()) {
                var jobCode = job.getJobCode();
                try {
                    var jobComponent = getJobExecutionComponent(jobCode);
                    var future = poolTaskScheduler.schedule(jobComponent,
                            new CronTrigger(job.getJobSchedule(), TimeZone.getTimeZone("Europe/Moscow")));
                    this.listenableFutureMap.put(jobComponent, future);
                    log.info("Job '{}' scheduled for '{}'", jobCode, job.getJobSchedule());
                } catch (SchedulerException ex) {
                    log.warn("Scheduled job with code '{}' not exists or disabled", jobCode);
                }
            }
        });
        log.info("Scheduled jobs: enabled");
    }

    public List<ScheduledJobDto> getAllScheduledJobs() {
        return scheduledJobService.getAllScheduledJobs().stream()
                .map(ScheduledJobEntity::toDto)
                .collect(Collectors.toList());
    }

    public ScheduledJobDto getScheduledJob(final String jobCode) {
        return scheduledJobService.getScheduledJobByCode(jobCode).toDto();
    }

    public ScheduledJobDto startScheduledJob(String jobCode) {
        var job = scheduledJobService.getScheduledJobByCode(jobCode);
        if (Objects.isNull(job)) {
            throw new SchedulerException(String.format(JOB_NOT_FOUND, jobCode));
        }
        if (JobStatus.RUNNING.equals(job.getJobStatus())) {
            return getScheduledJob(jobCode);
        }
        var jobComponent = getJobExecutionComponent(job.getJobCode());
        var future = poolTaskScheduler.schedule(jobComponent, new CronTrigger(job.getJobSchedule()));
        if (Objects.isNull(future)) {
            throw new SchedulerException(JOB_START_ERROR);
        }
        this.listenableFutureMap.put(jobComponent, future);
        log.info("Job '{}' started", job.getJobCode());
        job.setActive(true);
        scheduledJobService.saveJobState(job);
        return getScheduledJob(jobCode);
    }

    public ScheduledJobDto stopScheduledJob(String jobCode) {
        var job = scheduledJobService.getScheduledJobByCode(jobCode);
        if (Objects.isNull(job)) {
            throw new SchedulerException(String.format(JOB_NOT_FOUND, jobCode));
        }
        if (JobStatus.RUNNING.equals(job.getJobStatus())) {
            job.setJobStatus(JobStatus.STOPPED);
        }
        var jobComponent = getJobExecutionComponent(jobCode);
        var future = this.listenableFutureMap.get(jobComponent);
        if (Objects.nonNull(future)) {
            future.cancel(true);
        }
        this.listenableFutureMap.remove(jobComponent);
        log.info("Job '{}' stopped", jobCode);
        job.setActive(false);
        scheduledJobService.saveJobState(job);
        return getScheduledJob(jobCode);
    }

    public ScheduledJobDto startScheduledJobOnce(final String jobCode) {
        var job = scheduledJobService.getScheduledJobByCode(jobCode);
        if (Objects.isNull(job)) {
            throw new SchedulerException(String.format(JOB_NOT_FOUND, jobCode));
        }
        if (!JobStatus.RUNNING.equals(job.getJobStatus())) {
            var jobComponent = getJobExecutionComponent(jobCode);
            poolTaskScheduler.schedule(jobComponent, DateUtils.addSeconds(new Date(), 5));
            log.info("Job '{}' scheduled for single run", jobCode);
        }
        return getScheduledJob(jobCode);
    }

    public ScheduledJobDto changeSchedule(final String jobCode, String cronExpression) {
        if (!CronExpression.isValidExpression(cronExpression)) {
            throw new SchedulerException(INVALID_CRON_EXPRESSION);
        }
        stopScheduledJob(jobCode);
        var job = scheduledJobService.getScheduledJobByCode(jobCode);
        if (Objects.isNull(job)) {
            throw new SchedulerException(String.format(JOB_NOT_FOUND, jobCode));
        }
        job.setJobSchedule(cronExpression);
        scheduledJobService.saveJobState(job);
        startScheduledJob(jobCode);
        return getScheduledJob(jobCode);
    }

    public ScheduledJobDto changeParameters(final String jobCode, String parameters) {
        var job = scheduledJobService.getScheduledJobByCode(jobCode);
        if (Objects.isNull(job)) {
            throw new SchedulerException(String.format(JOB_NOT_FOUND, jobCode));
        }
        var jobComponent = getJobExecutionComponent(jobCode);
        try {
            job.setParameters(jobComponent.parseParameters(parameters));
            scheduledJobService.saveJobState(job);
        } catch (IOException ex) {
            throw new SchedulerException(INVALID_JOB_PARAMETERS);
        }
        return getScheduledJob(jobCode);
    }

    public InputStreamResource getLogContent(final String jobCode, String logCode) {
        var job = scheduledJobService.getScheduledJobByCode(jobCode);
        if (Objects.isNull(job)) {
            throw new SchedulerException(String.format(JOB_NOT_FOUND, jobCode));
        }
        var jobLoggerCode = jobCode + "Logger";
        var jobLogger = jobLoggers.get(jobLoggerCode);
        if (Objects.isNull(jobLogger)) {
            throw new SchedulerException(String.format(JOB_LOGGER_NOT_FOUND, jobLoggerCode));
        }
        if (Objects.isNull(logCode)) {
            var scheduledJobLastLog = scheduledJobLogService.getScheduledJobLastLog(jobCode);
            if (scheduledJobLastLog.isEmpty()) {
                return null;
            }
            logCode = scheduledJobLastLog.get().getLogCode();
        }
        return jobLogger.getLogContent(jobCode, logCode);
    }

    private ScheduledJob getJobExecutionComponent(String jobCode) {
        var jobComponent = jobThreads.get(jobCode);
        if (Objects.isNull(jobComponent)) {
            throw new SchedulerException(String.format(JOB_NOT_FOUND, jobCode));
        }
        return jobComponent;
    }

}