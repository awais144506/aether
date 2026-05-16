package com.ocp.aether.controller;

import com.ocp.aether.utility.LogBroadcaster;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/logs")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class LogStreamController {
    private final LogBroadcaster broadcaster;
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamLogs() {
        return broadcaster.addClient();
    }
}