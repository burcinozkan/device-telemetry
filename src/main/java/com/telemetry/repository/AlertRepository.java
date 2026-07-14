package com.telemetry.repository;

import com.telemetry.domain.Alert;
import com.telemetry.domain.AlertSeverity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository  extends JpaRepository<Alert, Long> {

    List<Alert> findByDeviceIdOrderByTriggeredAtDesc(String deviceId);
    List<Alert> findByResolvedFalseOrderByTriggeredAtDesc();
    long countByResolvedFalse();
    long countBySeverityAndResolvedFalse(AlertSeverity severity);

}
