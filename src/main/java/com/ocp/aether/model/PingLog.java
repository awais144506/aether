package com.ocp.aether.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ping_logs")
@Data
public class PingLog {
    @EmbeddedId
    private PingLogId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitor_id", nullable = false)
    private Monitor monitor;
    private int statusCode;
    private long responseTimeMs;
}
