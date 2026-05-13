package com.ocp.aether.service;

import com.ocp.aether.config.SecurityService;
import com.ocp.aether.dto.MonitorRequest;
import com.ocp.aether.model.Monitor;
import com.ocp.aether.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonitorService {

    private final SecurityService securityService;
    private final MonitorRepository monitorRepository;

    public Monitor addUrl(MonitorRequest request) throws Exception {
        Monitor monitor = new Monitor();
        monitor.setSiteName(request.getSiteName());
        monitor.setUrl(request.getUrl());
        try {
            String encrypted = securityService.encrypt(request.getEncryptedToken());
            monitor.setEncryptedToken(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Vault failure: Could not secure token.");
        }
        return monitorRepository.save(monitor);
    }

    public List<Monitor> getAllTargets() {
        return monitorRepository.findAll();
    }
}
