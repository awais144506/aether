package com.ocp.aether.utility;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class LogBroadcaster {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter addClient() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        return emitter;
    }

    public void broadcast(String monitorId, int statusCode, long latencyMs, String region) {
        LiveLog rawPayload = new LiveLog(monitorId, statusCode, latencyMs, region);

        for (SseEmitter emitter : emitters) {
            try {
                // Sending as a structured JSON data event named "pulse"
                emitter.send(SseEmitter.event()
                        .name("pulse")
                        .data(rawPayload));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }

    // High-performance payload container for clean frontend JSON translation
    public record LiveLog(String monitorId, int statusCode, long latencyMs, String region) {}
}