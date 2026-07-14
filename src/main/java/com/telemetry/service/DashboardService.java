package com.telemetry.service;

import com.telemetry.domain.DeviceStatus;
import com.telemetry.dto.DashboardSummaryResponse;
import com.telemetry.repository.AlertRepository;
import com.telemetry.repository.DeviceRepository;
import com.telemetry.repository.TelemetryEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DeviceRepository deviceRepository;
    private final AlertRepository alertRepository;
    private final TelemetryEventRepository telemetryEventRepository;

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary() {
        long totalDevices = deviceRepository.count();
        long onlineDevices = deviceRepository.countByStatus(DeviceStatus.ONLINE);
        long runningDevices = deviceRepository.countByStatus(DeviceStatus.RUNNING);
        long activeAlerts = alertRepository.countByResolvedFalse();
        long eventsLast24Hours = telemetryEventRepository.countSince(Instant.now().minus(24, ChronoUnit.HOURS));

        return DashboardSummaryResponse.builder()
                .totalDevices(totalDevices)
                .onlineDevices(onlineDevices)
                .runningDevices(runningDevices)
                .activeAlerts(activeAlerts)
                .eventsLast24Hours(eventsLast24Hours)
                .build();
    }
}
