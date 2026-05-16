package com.ocp.aether.service;

import com.ocp.aether.config.SecurityService;
import com.ocp.aether.dto.MonitorRequest;
import com.ocp.aether.master.MasterRegistry;
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
    private final MasterRegistry masterRegistry;
    private final MasterNeuralService neuralService;

    public Monitor addUrl(MonitorRequest request) throws Exception {
        Monitor monitor = new Monitor();
        monitor.setSiteName(request.getSiteName());
        monitor.setUrl(request.getUrl());
        monitor.setRegion(request.getRegion());
        if (Boolean.parseBoolean(request.getEncryptedToken())) {
            String encrypted = securityService.encrypt(request.getEncryptedToken());
            monitor.setEncryptedToken(encrypted);
        }
        Monitor saved = monitorRepository.save(monitor);
        masterRegistry.hydrate(List.of(saved));
        neuralService.pushNewMissionToRegion(saved);
        return saved;
    }

    public List<Monitor> getAllTargets() {
        return monitorRepository.findAll();
    }
}
