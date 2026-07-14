package com.telemetry.dto;

import com.telemetry.domain.DeviceStatus;
import com.telemetry.domain.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;

@Builder
public record TelemetryIngestRequest(
        @NotBlank String deviceId,
        @NotNull EventType eventType,
        Instant timestamp,
        DeviceStatus deviceStatus,
        Double powerWatts,
        Double temperatureCelsius,
        Double waterLiters,
        String programPhase,
        Boolean doorOpen,
        String errorCode
) {
}
