package com.telemetry.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String deviceId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private DeviceType type;

    @Column(nullable = false, length = 64)
    private String ownerId;

    @Column(length = 64)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private DeviceStatus status;

    @Column(nullable = false, updatable = false)
    private Instant registeredAt;

    @PrePersist
    void onCreate(){
        if (registeredAt == null){
            registeredAt = Instant.now();
        }
        if (status ==null){
            status= DeviceStatus.OFFLINE;
        }
    }
}
