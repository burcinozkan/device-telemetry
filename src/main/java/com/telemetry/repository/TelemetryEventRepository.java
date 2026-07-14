package com.telemetry.repository;

import com.telemetry.domain.TelemetryEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface TelemetryEventRepository extends JpaRepository<TelemetryEvent, Long> {

    Page<TelemetryEvent> findByDeviceIdOrderByEventTimestampDesc(String deviceId, Pageable pageable);

    @Query("SELECT COUNT(e) FROM TelemetryEvent e WHERE e.receivedAt >= :since")
    long countSince(@Param("since") Instant since);
}