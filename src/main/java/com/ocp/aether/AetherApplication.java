package com.ocp.aether;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AetherApplication {

    public static void main(String[] args) {
        SpringApplication.run(AetherApplication.class, args);
    }

}
