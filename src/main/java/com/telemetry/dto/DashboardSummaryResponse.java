package com.telemetry.dto;

import lombok.Builder;

@Builder
public record DashboardSummaryResponse(
        long totalDevices,
        long onlineDevices,
        long runningDevices,
        long activeAlerts,
        long eventsLast24Hours
) {
}
