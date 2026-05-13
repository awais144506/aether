package com.ocp.aether.repository;

import com.ocp.aether.model.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitorRepository extends JpaRepository<Monitor, String> {
}