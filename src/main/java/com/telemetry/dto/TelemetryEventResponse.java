package com.telemetry.dto;

import com.telemetry.domain.DeviceStatus;
import com.telemetry.domain.EventType;
import lombok.Builder;

import java.time.Instant;

@Builder
public record TelemetryEventResponse(
        Long id,
        String deviceId,
        EventType eventType,
        Instant eventTimestamp,
        DeviceStatus deviceStatus,
        Double powerWatts,
        Double temperatureCelsius,
        Double waterLiters,
        String programPhase,
        Boolean doorOpen,
        String errorCode,
        Instant receivedAt
) {
}
//kayıtedlen