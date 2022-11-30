package com.example.ApachePOIExcelExample.scheduler.entity;

import com.example.ApachePOIExcelExample.scheduler.enums.JobResult;
import com.example.ApachePOIExcelExample.scheduler.enums.JobStatus;
import com.example.ApachePOIExcelExample.scheduler.model.ScheduledJobDto;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@Table(name = "scheduled_job")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ScheduledJobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "job_code", unique = true, nullable = false)
    private String jobCode;

    @Column(name = "last_start_time")
    private Instant lastStartTime;

    @Column(name = "last_finish_time")
    private Instant lastFinishTime;

    @Column(name = "last_success_start_time")
    private Instant lastSuccessStartTime;

    @Column(name = "job_result")
    @Enumerated(value = EnumType.STRING)
    private JobResult jobResult;

    @Column(name = "job_status")
    @Enumerated(value = EnumType.STRING)
    private JobStatus jobStatus;

    @Column(name = "job_schedule")
    private String jobSchedule;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "title")
    private String title;

    @Type(type = "jsonb")
    @Column(name = "parameters", columnDefinition = "jsonb")
    private String parameters;

    public ScheduledJobDto toDto() {
        return ScheduledJobDto.builder()
                .id(this.id)
                .jobCode(this.jobCode)
                .lastStartTime(this.lastStartTime)
                .lastFinishTime(this.lastFinishTime)
                .lastSuccessStartTime(this.lastSuccessStartTime)
                .jobResult(this.jobResult)
                .jobStatus(this.jobStatus)
                .jobSchedule(this.jobSchedule)
                .isActive(this.isActive)
                .title(this.title)
                .parameters(this.parameters)
                .build();
    }

}