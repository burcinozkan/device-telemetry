package com.telemetry.service;


import com.telemetry.domain.DeviceStatus;
import com.telemetry.domain.TelemetryEvent;
import com.telemetry.dto.DeviceStateResponse;
import com.telemetry.dto.TelemetryEventResponse;
import com.telemetry.dto.TelemetryIngestRequest;
import com.telemetry.kafka.TelemetryEventProducer;
import com.telemetry.repository.TelemetryEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelemetryService {

    private final DeviceService deviceService;
    private final TelemetryEventRepository telemetryEventRepository;
    private final DeviceStateCacheService deviceStateCacheService;
    private final TelemetryEventProducer telemetryEventProducer;

    @Transactional
    public TelemetryEventResponse ingest(TelemetryIngestRequest request){

        deviceService.findDevice(request.deviceId());
        TelemetryEvent event = TelemetryEvent.builder()
                .deviceId(request.deviceId())
                .eventType(request.eventType())
                .eventTimestamp(request.timestamp() != null ? request.timestamp(): Instant.now())
                .deviceStatus(request.deviceStatus())
                .powerWatts(request.powerWatts())
                .temperatureCelsius(request.temperatureCelsius())
                .waterLiters(request.waterLiters())
                .programPhase(request.programPhase())
                .doorOpen(request.doorOpen())
                .errorCode(request.errorCode())
                .build();

        TelemetryEvent saved = telemetryEventRepository.save(event);

        if (request.deviceStatus() != null) {
            deviceService.updateStatus(request.deviceId(), request.deviceStatus());
        } else if (request.eventType().name().contains("HEARTBEAT")) {
            deviceService.updateStatus(request.deviceId(), DeviceStatus.ONLINE);
        }
        deviceStateCacheService.cacheLatestState(saved);
        telemetryEventProducer.publish(saved);
        return toResponse(saved);
    }

    // kafka-redis

    private TelemetryEventResponse toResponse(TelemetryEvent event){
        return TelemetryEventResponse.builder()
                .id(event.getId())
                .deviceId(event.getDeviceId())
                .eventType(event.getEventType())
                .eventTimestamp(event.getEventTimestamp())
                .deviceStatus(event.getDeviceStatus())
                .powerWatts(event.getPowerWatts())
                .temperatureCelsius(event.getTemperatureCelsius())
                .waterLiters(event.getWaterLiters())
                .programPhase(event.getProgramPhase())
                .doorOpen(event.getDoorOpen())
                .errorCode(event.getErrorCode())
                .receivedAt(event.getReceivedAt())
                .build();
    }



    @Transactional
    public List<TelemetryEventResponse> getHistory(String deviceId, int page, int size){
        deviceService.findDevice(deviceId);
        Page<TelemetryEvent> events = telemetryEventRepository
                .findByDeviceIdOrderByEventTimestampDesc(deviceId, PageRequest.of(page,size));
        return events.stream().map(this::toResponse).toList();
    }
    public DeviceStateResponse getLatestState(String deviceId) {
        deviceService.findDevice(deviceId);
        return deviceStateCacheService.getLatestState(deviceId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No cached state for device: " + deviceId));
    }
}
