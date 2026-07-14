package com.telemetry.service.alert;

import com.telemetry.domain.AlertSeverity;
import com.telemetry.domain.TelemetryEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OverTemperatureRule implements AlertRule {

    private final double thresholdCelsius;

    public OverTemperatureRule(@Value("${telemetry.alerts.high-temperature-threshold-celsius}") double thresholdCelsius) {
        this.thresholdCelsius = thresholdCelsius;
    }

    @Override
    public String getRuleName() {
        return "OVER_TEMPERATURE";
    }

    @Override
    public Optional<AlertTrigger> evaluate(TelemetryEvent event) {
        if (event.getTemperatureCelsius() == null || event.getTemperatureCelsius() <= thresholdCelsius) {
            return Optional.empty();
        }
        return Optional.of(AlertTrigger.builder()
                .ruleName(getRuleName())
                .severity(AlertSeverity.HIGH)
                .message("Device %s temperature %.1f°C exceeds threshold %.1f°C"
                        .formatted(event.getDeviceId(), event.getTemperatureCelsius(), thresholdCelsius))
                .build());
    }
}
