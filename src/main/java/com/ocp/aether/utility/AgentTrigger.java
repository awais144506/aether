package com.ocp.aether.utility;

import com.ocp.aether.master.MasterRegistry;
import com.ocp.aether.model.Monitor;
import com.ocp.aether.repository.MonitorRepository;
import com.ocp.aether.service.AgentNeuralClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
@RequiredArgsConstructor// Run after the Master's hydration
public class AgentTrigger implements CommandLineRunner {

    private final AgentNeuralClient agentClient;
    private final MonitorRepository monitorRepository;
    private final MasterRegistry masterRegistry;


    @Override
    public void run(String... args) {
        List<Monitor> allMonitors = monitorRepository.findAll();

        // 2. LOG THE COUNT: If this says 0, your DB is empty or connection is wrong
        System.out.println("DATABASE CHECK: Found " + allMonitors.size() + " monitors in PostgreSQL.");

        if (!allMonitors.isEmpty()) {
            // 3. Push to RAM
            masterRegistry.hydrate(allMonitors);
        }
        agentClient.startNervousSystem("localhost", 9001);
    }
}