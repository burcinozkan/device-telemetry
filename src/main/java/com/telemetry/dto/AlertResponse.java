package com.telemetry.dto;

import com.telemetry.domain.AlertSeverity;
import lombok.Builder;

import java.time.Instant;

@Builder
public record AlertResponse(
        Long id,
        String deviceId,
        String ruleName,
        AlertSeverity severity,
        String message,
        Long telemetryEventId,
        boolean resolved,
        Instant triggeredAt
) {

    // uyarı
}
