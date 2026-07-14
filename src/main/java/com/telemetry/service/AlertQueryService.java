package com.telemetry.service;

import com.telemetry.domain.Alert;
import com.telemetry.dto.AlertResponse;
import com.telemetry.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertQueryService {

    private final AlertRepository alertRepository;

    @Transactional(readOnly = true)
    public List<AlertResponse> getActiveAlerts() {
        return alertRepository.findByResolvedFalseOrderByTriggeredAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AlertResponse> getAlertsByDevice(String deviceId) {
        return alertRepository.findByDeviceIdOrderByTriggeredAtDesc(deviceId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AlertResponse resolveAlert(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alert not found: " + alertId));
        alert.setResolved(true);
        return toResponse(alertRepository.save(alert));
    }

    private AlertResponse toResponse(Alert alert) {
        return AlertResponse.builder()
                .id(alert.getId())
                .deviceId(alert.getDeviceId())
                .ruleName(alert.getRuleName())
                .severity(alert.getSeverity())
                .message(alert.getMessage())
                .telemetryEventId(alert.getTelemetryEventId())
                .resolved(alert.isResolved())
                .triggeredAt(alert.getTriggeredAt())
                .build();
    }
}
