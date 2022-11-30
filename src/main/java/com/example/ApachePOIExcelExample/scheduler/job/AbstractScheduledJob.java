package com.example.ApachePOIExcelExample.scheduler.job;

import com.example.ApachePOIExcelExample.exception.SchedulerException;
import com.example.ApachePOIExcelExample.scheduler.entity.ScheduledJobEntity;
import com.example.ApachePOIExcelExample.scheduler.enums.JobResult;
import com.example.ApachePOIExcelExample.scheduler.enums.JobStatus;
import com.example.ApachePOIExcelExample.scheduler.service.ScheduledJobLogService;
import com.example.ApachePOIExcelExample.scheduler.service.ScheduledJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.example.ApachePOIExcelExample.exception.SchedulerException.JOB_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractScheduledJob implements ScheduledJob {

    protected final ScheduledJobService scheduledJobService;
    protected final ScheduledJobLogService loggingService;
    protected final PlatformTransactionManager transactionManager;

    public void run(final String jobCode, final boolean isTransactional) {
        log.info("Start Scheduled Job with code '{}'", jobCode);
        var startDateTime = Instant.now();
        var logCode = String.format("%s_%s",
                jobCode,
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                .withZone(ZoneId.of("Europe/Moscow"))
                .format(startDateTime)
        );
        this.launchScheduledJob(jobCode, logCode, startDateTime);
        if (isTransactional) {
            this.launchBusinessLogicWithinTransaction(jobCode, logCode);
        } else {
            this.launchBusinessLogicWithoutTransaction(jobCode, logCode);
        }
    }

    private void launchScheduledJob(final String jobCode, final String logCode, final Instant startDateTime) {
        final var setRunningTransaction = new DefaultTransactionDefinition();
        final var setRunningStatus = transactionManager.getTransaction(setRunningTransaction);
        try {
            final var job = scheduledJobService.getScheduledJobByCode(jobCode);
            if (Objects.isNull(job)) {
                throw new SchedulerException(String.format(JOB_NOT_FOUND, jobCode));
            }
            if (JobStatus.RUNNING.equals(job.getJobStatus())) {
                log.info("Stop Scheduled Job with code '{}', Reason: Job is already running", jobCode);
                transactionManager.rollback(setRunningStatus);
                loggingService.error(logCode, Instant.now());
                return;
            }
            readParameters(job);
            job.setJobStatus(JobStatus.RUNNING);
            job.setLastStartTime(startDateTime);
            log.info("Scheduled Job with code '{}' status is RUNNING now", jobCode);
            transactionManager.commit(setRunningStatus);
            loggingService.start(jobCode, logCode, startDateTime);
        } catch (Exception ex) {
            log.info("Scheduled Job with code '{}' failed to change in RUNNING status", jobCode);
            log.info(ex.getMessage(), ex);
            transactionManager.rollback(setRunningStatus);
            loggingService.error(logCode, Instant.now());
            throw ex;
        }
    }

    private void readParameters(final ScheduledJobEntity job) {
        try {
            this.parseParameters(job.getParameters());
            log.info("Run job with parameters: {}", job.getParameters());
        } catch (IOException ex) {
            log.error("Error parsing scheduled job parameters: {}, error: {}", job.getParameters(), ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    private void launchBusinessLogicWithinTransaction(final String jobCode, final String logCode) {
        final var transactionDefinition = new DefaultTransactionDefinition();
        final var transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            final var job = scheduledJobService.getScheduledJobByCode(jobCode);
            final var success = this.perform(job, logCode);

            final var finishDateTime = Instant.now();
            final var jobResult = success ? JobResult.SUCCESS : JobResult.FINISHED_WITH_ERRORS;
            job.setJobStatus(JobStatus.FINISHED);
            job.setLastFinishTime(finishDateTime);
            job.setLastSuccessStartTime(job.getLastStartTime());
            job.setJobResult(jobResult);
            log.info("Successfully finish Scheduled Job with code '{}'", jobCode);
            transactionManager.commit(transactionStatus);
            loggingService.finish(logCode, finishDateTime, jobResult);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollback(transactionStatus);

            final var rollBackDefinition = new DefaultTransactionDefinition();
            final var rollBackJobStatus = transactionManager.getTransaction(rollBackDefinition);
            log.error(String.format("Stop Scheduled Job with code '%s', Reason: %s", jobCode, ex.getMessage()), ex);
            final var job = scheduledJobService.getScheduledJobByCode(jobCode);
            final var finishDateTime = Instant.now();
            job.setJobStatus(JobStatus.FINISHED);
            job.setLastFinishTime(finishDateTime);
            job.setJobResult(JobResult.FAILED);
            transactionManager.commit(rollBackJobStatus);
            loggingService.error(logCode, finishDateTime);
        }
    }

    private void launchBusinessLogicWithoutTransaction(final String jobCode, final String logCode) {
        try {
            final var success = this.perform(scheduledJobService.getScheduledJobByCode(jobCode), logCode);

            final var transactionDefinition = new DefaultTransactionDefinition();
            final var transactionStatus = transactionManager.getTransaction(transactionDefinition);
            final var job = scheduledJobService.getScheduledJobByCode(jobCode);
            final var finishDateTime = Instant.now();
            final var jobResult = success ? JobResult.SUCCESS : JobResult.FINISHED_WITH_ERRORS;
            job.setJobStatus(JobStatus.FINISHED);
            job.setLastFinishTime(finishDateTime);
            job.setLastSuccessStartTime(job.getLastStartTime());
            job.setJobResult(jobResult);
            log.info("Successfully finish Scheduled Job with code '{}'", jobCode);
            transactionManager.commit(transactionStatus);
            loggingService.finish(logCode, finishDateTime, jobResult);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);

            final var rollBackDefinition = new DefaultTransactionDefinition();
            final var rollBackJobStatus = transactionManager.getTransaction(rollBackDefinition);
            log.error(String.format("Stop Scheduled Job with code '%s', Reason: %s", jobCode, ex.getMessage()), ex);
            final var job = scheduledJobService.getScheduledJobByCode(jobCode);
            final var finishDateTime = Instant.now();
            job.setJobStatus(JobStatus.FINISHED);
            job.setLastFinishTime(finishDateTime);
            job.setJobResult(JobResult.FAILED);
            transactionManager.commit(rollBackJobStatus);
            loggingService.error(logCode, finishDateTime);
        }
    }

    public abstract boolean perform(final ScheduledJobEntity job, final String logCode);

}