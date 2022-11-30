package com.example.ApachePOIExcelExample.scheduler.entity;

import com.example.ApachePOIExcelExample.scheduler.enums.JobResult;
import com.example.ApachePOIExcelExample.scheduler.model.ScheduledJobLogDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@Table(name = "scheduled_job_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledJobLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "job_code", nullable = false)
    private String jobCode;

    @Column(name = "log_code", unique = true, nullable = false)
    private String logCode;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "finish_time")
    private Instant finishTime;

    @Column(name = "job_result")
    @Enumerated(value = EnumType.STRING)
    private JobResult jobResult;

    public ScheduledJobLogDto toDto() {
        return ScheduledJobLogDto.builder()
                .id(this.id)
                .jobCode(this.jobCode)
                .logCode(this.logCode)
                .startTime(this.startTime)
                .finishTime(this.finishTime)
                .jobResult(this.jobResult)
                .build();
    }

}