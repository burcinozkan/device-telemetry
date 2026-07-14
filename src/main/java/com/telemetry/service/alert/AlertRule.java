package com.telemetry.service.alert;

import com.telemetry.domain.AlertSeverity;
import com.telemetry.domain.TelemetryEvent;
import lombok.Builder;

import java.util.Optional;

public interface AlertRule {

    String getRuleName();

    Optional<AlertTrigger> evaluate(TelemetryEvent event);

    @Builder
    record AlertTrigger(String ruleName, AlertSeverity severity, String message) {
    }
}
