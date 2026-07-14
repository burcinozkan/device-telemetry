package com.telemetry.service.alert;

import com.telemetry.domain.AlertSeverity;
import com.telemetry.domain.TelemetryEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ExcessiveWaterUsageRule implements AlertRule {

    private final double thresholdLiters;

    public ExcessiveWaterUsageRule(@Value("${telemetry.alerts.high-water-usage-liters}") double thresholdLiters) {
        this.thresholdLiters = thresholdLiters;
    }

    @Override
    public String getRuleName() {
        return "EXCESSIVE_WATER_USAGE";
    }

    @Override
    public Optional<AlertTrigger> evaluate(TelemetryEvent event) {
        if (event.getWaterLiters() == null || event.getWaterLiters() <= thresholdLiters) {
            return Optional.empty();
        }
        return Optional.of(AlertTrigger.builder()
                .ruleName(getRuleName())
                .severity(AlertSeverity.MEDIUM)
                .message("Device %s water usage %.1fL exceeds threshold %.1fL"
                        .formatted(event.getDeviceId(), event.getWaterLiters(), thresholdLiters))
                .build());
    }
}
