package com.telemetry.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String deviceId;

    @Column(nullable = false, length = 64)
    private String ruleName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private AlertSeverity severity;

    @Column(nullable = false, length = 512)
    private String message;

    @Column
    private Long telemetryEventId;
    //hangi telemetri event’i bu uyarıyı tetikledi

    @Column(nullable = false)
    private boolean resolved; //cozuldu mu

    @Column(nullable = false, updatable = false)
    private Instant triggeredAt;

    /*
    * Instant, Java’da tek bir zaman anını tutan sınıftır (java.time paketinde).

UTC bazlıdır; “14 Temmuz 2026, 00:35:10Z”*/


    @PrePersist
    void onCreate() {
        if (triggeredAt == null) {
            triggeredAt = Instant.now();
        }
    }

    /*dbye kayıt anında triggerdat null ise now ile doldurudr.*/
}
