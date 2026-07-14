package com.telemetry.service;

import com.telemetry.domain.Device;
import com.telemetry.domain.DeviceStatus;
import com.telemetry.dto.DeviceResponse;
import com.telemetry.dto.RegisterDeviceRequest;
import com.telemetry.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    @Transactional
    public DeviceResponse getByDeviceId(String deviceId){
        return toResponse(findDevice(deviceId));
    }

    @Transactional
    public List<DeviceResponse> listByOwner(String ownerId){
        return deviceRepository.findByOwnerId(ownerId).stream().map(this::toResponse).toList();
    }
    //register

    @Transactional
    public List<DeviceResponse> listAll(){
        return deviceRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public DeviceResponse register(RegisterDeviceRequest request){
        if (deviceRepository.existsByDeviceId(request.deviceId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Device already exists");
            // 409 : duplicate resources
        }

        Device device = Device.builder()
                .deviceId(request.deviceId())
                .name(request.name())
                .type(request.type())
                .ownerId(request.ownerId())
                .lastSeenAt(Instant.now())
                .location(request.location())
                .status(DeviceStatus.OFFLINE)
                .build();

        return toResponse(deviceRepository.save(device));
    }


    private DeviceResponse toResponse(Device device){
        // device(entity) -> DeviceResponse(DTO)

        return DeviceResponse.builder()
                .id(device.getId())
                .deviceId(device.getDeviceId())
                .name(device.getName())
                .deviceType(device.getType())
                .ownerId(device.getOwnerId())
                .location(device.getLocation())
                .status(device.getStatus())
                .registeredAt(device.getRegisteredAt())
                .build();
    }

    Device findDevice(String deviceId){
        return deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found:" + deviceId));

    }

    @Transactional
    public void updateStatus(String deviceId, DeviceStatus status){
        Device device = findDevice(deviceId);
        device.setStatus(status);
        deviceRepository.save(device);
    }





}
