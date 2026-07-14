package com.telemetry.service.alert;

import com.telemetry.domain.Alert;
import com.telemetry.domain.TelemetryEvent;
import com.telemetry.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertAnalysisService {

    private final List<AlertRule> rules;
    private final AlertRepository alertRepository;

    @Transactional
    public List<Alert> analyze(TelemetryEvent event) {
        List<Alert> triggeredAlerts = new ArrayList<>();

        for (AlertRule rule : rules) {
            rule.evaluate(event).ifPresent(trigger -> {
                Alert alert = Alert.builder()
                        .deviceId(event.getDeviceId())
                        .ruleName(trigger.ruleName())
                        .severity(trigger.severity())
                        .message(trigger.message())
                        .telemetryEventId(event.getId())
                        .resolved(false)
                        .build();
                triggeredAlerts.add(alertRepository.save(alert));
            });
        }

        return triggeredAlerts;
    }
}
