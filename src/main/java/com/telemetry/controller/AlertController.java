package com.telemetry.controller;

import com.telemetry.dto.AlertResponse;
import com.telemetry.service.AlertQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertQueryService alertQueryService;

    @GetMapping
    public List<AlertResponse> getActiveAlerts() {
        return alertQueryService.getActiveAlerts();
    }

    @GetMapping("/device/{deviceId}")
    public List<AlertResponse> getAlertsByDevice(@PathVariable String deviceId) {
        return alertQueryService.getAlertsByDevice(deviceId);
    }

    @PostMapping("/{alertId}/resolve")
    public AlertResponse resolveAlert(@PathVariable Long alertId) {
        return alertQueryService.resolveAlert(alertId);
    }
}
