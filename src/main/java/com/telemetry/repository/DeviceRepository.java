package com.telemetry.repository;

import com.telemetry.domain.Device;
import com.telemetry.domain.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    Optional<Device> findByDeviceId(String deviceId);

    boolean existsByDeviceId(String deviceId);

    List<Device> findByOwnerId(String ownerId);

    long countByStatus(DeviceStatus status);
}
