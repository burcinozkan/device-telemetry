package com.telemetry.dto;

import com.telemetry.domain.DeviceStatus;
import com.telemetry.domain.EventType;
import lombok.Builder;

import java.time.Instant;
@Builder
public record DeviceStateResponse(

        String deviceId,
        DeviceStatus status,
        EventType lastEventType,
        Instant lastSeenAt,
        Double powerWatts,
        Double temperatureCelsius,
        Double waterLiters,
        String programPhase,
        Boolean doorOpen,
        String errorCode
) {

    //redisteki anlıkdurum
}
