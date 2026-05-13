package com.ocp.aether.repository;

import com.ocp.aether.model.PingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PingLogRepository extends JpaRepository<PingLog, String> {
    // Allows you to fetch the history for a specific monitor
}