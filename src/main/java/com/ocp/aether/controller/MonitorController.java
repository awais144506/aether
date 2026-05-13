package com.ocp.aether.controller;

import com.ocp.aether.dto.MonitorRequest;
import com.ocp.aether.model.Monitor;
import com.ocp.aether.service.MonitorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {
    private final MonitorService monitorService;

    public MonitorController(MonitorService monitorService)
    {
        this.monitorService = monitorService;
    }

    @PostMapping
    public ResponseEntity<Monitor> registerUrl(@Valid @RequestBody MonitorRequest request) throws Exception {
        Monitor saved = monitorService.addUrl(request);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Monitor> getAllRegisteredTargets() {
        return monitorService.getAllTargets();
    }
}
