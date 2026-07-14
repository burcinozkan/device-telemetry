package com.telemetry.dto;

import com.telemetry.domain.DeviceStatus;
import com.telemetry.domain.DeviceType;
import lombok.Builder;

import java.time.Instant;
//@Builder (Lombok) bu .builder()...build() kodunu senin yerine üretir.
@Builder
public record DeviceResponse(
        Long id,
        String deviceId,
        String name,
        DeviceType deviceType,
        String ownerId,
        String location,
        DeviceStatus status,
        Instant registeredAt
) {

    //cihaz bilgisi



}
