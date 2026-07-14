package com.telemetry.service.alert;

import com.telemetry.domain.AlertSeverity;
import com.telemetry.domain.TelemetryEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HighPowerConsumptionRule implements AlertRule {

    private final double thresholdWatts;

    public HighPowerConsumptionRule(@Value("${telemetry.alerts.high-power-threshold-watts}") double thresholdWatts) {
        this.thresholdWatts = thresholdWatts;
    }

    @Override
    public String getRuleName() {
        return "HIGH_POWER_CONSUMPTION";
    }

    @Override
    public Optional<AlertTrigger> evaluate(TelemetryEvent event) {
        if (event.getPowerWatts() == null || event.getPowerWatts() <= thresholdWatts) {
            return Optional.empty();
        }
        return Optional.of(AlertTrigger.builder()
                .ruleName(getRuleName())
                .severity(AlertSeverity.HIGH)
                .message("Device %s power consumption %.0fW exceeds threshold %.0fW"
                        .formatted(event.getDeviceId(), event.getPowerWatts(), thresholdWatts))
                .build());
    }
}
