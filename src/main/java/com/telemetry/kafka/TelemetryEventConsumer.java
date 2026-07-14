package com.telemetry.kafka;

import com.telemetry.domain.DeviceStatus;
import com.telemetry.domain.EventType;
import com.telemetry.domain.TelemetryEvent;
import com.telemetry.repository.TelemetryEventRepository;
import com.telemetry.service.alert.AlertAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelemetryEventConsumer {

    private final ObjectMapper objectMapper;
    private final TelemetryEventRepository telemetryEventRepository;
    private final AlertAnalysisService alertAnalysisService;
    @KafkaListener(topics = "${telemetry.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String payload) {
        try {
            TelemetryEventProducer.TelemetryEventMessage message =
                    objectMapper.readValue(payload, TelemetryEventProducer.TelemetryEventMessage.class);

            TelemetryEvent event = telemetryEventRepository.findById(message.eventId())
                    .orElseGet(() -> reconstruct(message));

            var alerts = alertAnalysisService.analyze(event);
            if (!alerts.isEmpty()) {
                log.warn("Generated {} alert(s) for device {}", alerts.size(), event.getDeviceId());
            }
        } catch (Exception ex) {
            log.error("Failed to process telemetry event from Kafka", ex);
        }
    }

    private TelemetryEvent reconstruct(TelemetryEventProducer.TelemetryEventMessage message) {
        return TelemetryEvent.builder()
                .id(message.eventId())
                .deviceId(message.deviceId())
                .eventType(EventType.valueOf(message.eventType()))
                .eventTimestamp(Instant.parse(message.eventTimestamp()))
                .deviceStatus(message.deviceStatus() != null ? DeviceStatus.valueOf(message.deviceStatus()) : null)
                .powerWatts(message.powerWatts())
                .temperatureCelsius(message.temperatureCelsius())
                .waterLiters(message.waterLiters())
                .programPhase(message.programPhase())
                .doorOpen(message.doorOpen())
                .errorCode(message.errorCode())
                .build();
    }
}
