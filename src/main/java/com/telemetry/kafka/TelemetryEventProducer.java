package com.telemetry.kafka;

import com.telemetry.domain.TelemetryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelemetryEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${telemetry.kafka.topic}")
    private String topic;

    public void publish(TelemetryEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(new TelemetryEventMessage(
                    event.getId(),
                    event.getDeviceId(),
                    event.getEventType().name(),
                    event.getEventTimestamp().toString(),
                    event.getDeviceStatus() != null ? event.getDeviceStatus().name() : null,
                    event.getPowerWatts(),
                    event.getTemperatureCelsius(),
                    event.getWaterLiters(),
                    event.getProgramPhase(),
                    event.getDoorOpen(),
                    event.getErrorCode()
            ));
            kafkaTemplate.send(topic, event.getDeviceId(), payload);
            log.info("Published telemetry event {} for device {}", event.getId(), event.getDeviceId());
        } catch (JacksonException ex) {
            throw new IllegalStateException("Failed to publish telemetry event", ex);
        }
    }

    public record TelemetryEventMessage(
            Long eventId,
            String deviceId,
            String eventType,
            String eventTimestamp,
            String deviceStatus,
            Double powerWatts,
            Double temperatureCelsius,
            Double waterLiters,
            String programPhase,
            Boolean doorOpen,
            String errorCode
    ) {
    }
}
