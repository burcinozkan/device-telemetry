package com.telemetry.dto;

import com.telemetry.domain.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RegisterDeviceRequest(
        @NotBlank String deviceId,
        @NotBlank String name,
        @NotNull DeviceType type,
        @NotBlank String ownerId,
        String location
) {
}
