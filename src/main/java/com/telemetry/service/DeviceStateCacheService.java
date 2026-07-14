package com.telemetry.service;

import com.telemetry.domain.TelemetryEvent;
import com.telemetry.dto.DeviceStateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceStateCacheService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${telemetry.redis.device-state-prefix}")
    private String keyPrefix;

    @Value("${telemetry.redis.device-state-ttl-minutes}")
    private long ttlMinutes;

    public void cacheLatestState(TelemetryEvent telemetryEvent) {
        DeviceStateResponse stateResponse = DeviceStateResponse.builder()
                .deviceId(telemetryEvent.getDeviceId())
                .status(telemetryEvent.getDeviceStatus())
                .lastEventType(telemetryEvent.getEventType())
                .lastSeenAt(telemetryEvent.getEventTimestamp())
                .powerWatts(telemetryEvent.getPowerWatts())
                .temperatureCelsius(telemetryEvent.getTemperatureCelsius())
                .waterLiters(telemetryEvent.getWaterLiters())
                .programPhase(telemetryEvent.getProgramPhase())
                .doorOpen(telemetryEvent.getDoorOpen())
                .errorCode(telemetryEvent.getErrorCode())
                .build();

        try {
            String json = objectMapper.writeValueAsString(stateResponse);
            redisTemplate.opsForValue().set(
                    key(telemetryEvent.getDeviceId()),
                    json,
                    Duration.ofMinutes(ttlMinutes)
            );
        } catch (JacksonException ex) {
            throw new IllegalStateException("Failed to serialize device state", ex);
        }
    }

    public Optional<DeviceStateResponse> getLatestState(String deviceId) {
        String json = redisTemplate.opsForValue().get(key(deviceId));
        if (json == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(json, DeviceStateResponse.class));
        } catch (JacksonException ex) {
            throw new IllegalStateException("Failed to deserialize device state", ex);
        }
    }

    private String key(String deviceId) {
        return keyPrefix + deviceId;
    }
}
