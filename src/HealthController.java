
package com.queuecare.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public Map<String, String> health() {
        return Map.of(
            "application", "QueueCare",
            "status", "running",
            "message", "Clinic API is ready!"
        );
    }
}