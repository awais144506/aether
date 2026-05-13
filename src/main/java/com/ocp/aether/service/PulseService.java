package com.ocp.aether.service;

import com.ocp.aether.config.SecurityService;
import com.ocp.aether.model.Monitor;
import com.ocp.aether.model.PingLog;
import com.ocp.aether.model.PingLogId;
import com.ocp.aether.repository.MonitorRepository;
import com.ocp.aether.repository.PingLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class PulseService {
    private final MonitorRepository monitorRepository;
    private final SecurityService securityService;
    private final PingLogRepository pingLogRepository;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    @Scheduled(fixedRate = 30000)
    public void startSystemPulse() {
        List<Monitor> targets = monitorRepository.findAll();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (Monitor target : targets) {
                executor.submit(() -> processHeartbeat(target));
            }
        }
    }

    private void processHeartbeat(Monitor target) {
        long startTime = System.currentTimeMillis();
        int statusCode = 0;
        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(target.getUrl()))
                    .GET()
                    .build();
            var response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            statusCode = response.statusCode();
            target.setStatus(statusCode < 300 ? "UP ✅" : "ISSUE ⚠️");
        } catch (Exception e) {
            target.setStatus("DOWN ❌");
        } finally {
            long latency = System.currentTimeMillis() - startTime;
            savePulseToVault(target, statusCode, latency);
        }
    }

    @Transactional
    protected void savePulseToVault(Monitor target, int code, long latency) {
        monitorRepository.save(target);
        PingLog log = new PingLog();
        PingLogId logId = new PingLogId(UUID.randomUUID(), OffsetDateTime.now());
        log.setId(logId);
        log.setMonitor(target);
        log.setStatusCode(code);
        log.setResponseTimeMs(latency);
        pingLogRepository.save(log);
    }
}
