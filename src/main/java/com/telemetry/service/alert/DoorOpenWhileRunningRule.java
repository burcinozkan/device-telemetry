package com.telemetry.service.alert;

import com.telemetry.domain.AlertSeverity;
import com.telemetry.domain.DeviceStatus;
import com.telemetry.domain.TelemetryEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DoorOpenWhileRunningRule implements AlertRule {

    @Override
    public String getRuleName() {
        return "DOOR_OPEN_WHILE_RUNNING";
    }

    @Override
    public Optional<AlertTrigger> evaluate(TelemetryEvent event) {
        if (!Boolean.TRUE.equals(event.getDoorOpen())) {
            return Optional.empty();
        }
        if (event.getDeviceStatus() != DeviceStatus.RUNNING) {
            return Optional.empty();
        }
        return Optional.of(AlertTrigger.builder()
                .ruleName(getRuleName())
                .severity(AlertSeverity.CRITICAL)
                .message("Device %s reported door open while running".formatted(event.getDeviceId()))
                .build());
    }
}
