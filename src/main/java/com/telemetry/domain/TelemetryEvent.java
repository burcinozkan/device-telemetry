package com.telemetry.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "telemetry_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelemetryEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String deviceId;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private EventType eventType;

    @Column(nullable = false)
    private Instant eventTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private DeviceStatus deviceStatus;

    @Column
    private Double powerWatts;

    @Column
    private Double temperatureCelsius;

    @Column
    private Double waterLiters;

    @Column(length = 64)
    private String programPhase;

    @Column
    private Boolean doorOpen;

    @Column(length = 256)
    private String errorCode;

    @Column(nullable = false, updatable = false)
    private Instant receivedAt;

    @PrePersist
    void onCreate(){
        if (receivedAt == null){
            receivedAt = Instant.now();
        }
        if (eventTimestamp == null){
            eventTimestamp=receivedAt;
        }
    }
}
