package com.telemetry.service.alert;

import com.telemetry.domain.AlertSeverity;
import com.telemetry.domain.EventType;
import com.telemetry.domain.TelemetryEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DeviceErrorReportRule implements AlertRule {

    @Override
    public String getRuleName() {
        return "DEVICE_ERROR_REPORT";
    }

    @Override
    public Optional<AlertTrigger> evaluate(TelemetryEvent event) {
        if (event.getEventType() != EventType.ERROR_REPORT && event.getErrorCode() == null) {
            return Optional.empty();
        }
        String code = event.getErrorCode() != null ? event.getErrorCode() : "UNKNOWN";
        return Optional.of(AlertTrigger.builder()
                .ruleName(getRuleName())
                .severity(AlertSeverity.CRITICAL)
                .message("Device %s reported error code %s".formatted(event.getDeviceId(), code))
                .build());
    }
}
