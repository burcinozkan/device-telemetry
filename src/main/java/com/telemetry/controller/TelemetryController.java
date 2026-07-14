package com.telemetry.controller;

import com.telemetry.dto.DeviceStateResponse;
import com.telemetry.dto.TelemetryEventResponse;
import com.telemetry.dto.TelemetryIngestRequest;
import com.telemetry.service.TelemetryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/telemetry")
@RequiredArgsConstructor
public class TelemetryController {

    private final TelemetryService telemetryService;

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TelemetryEventResponse ingest(@Valid @RequestBody TelemetryIngestRequest request) {
        return telemetryService.ingest(request);
    }

    @GetMapping("/devices/{deviceId}/history")
    public List<TelemetryEventResponse> history(
            @PathVariable String deviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return telemetryService.getHistory(deviceId, page, size);
    }

    @GetMapping("/devices/{deviceId}/status")
    public DeviceStateResponse getStatus(@PathVariable String deviceId) {
        return telemetryService.getLatestState(deviceId);
    }
}
