package com.telemetry.service;


import com.telemetry.domain.Device;
import com.telemetry.domain.DeviceStatus;
import com.telemetry.domain.DeviceType;
import com.telemetry.dto.DeviceResponse;
import com.telemetry.dto.RegisterDeviceRequest;
import com.telemetry.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {


    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;


    @Test
    void shouldRegisterDeviceSuccessfully(){

        RegisterDeviceRequest request = new RegisterDeviceRequest(
                "device-1",
                "Dryer",
                 DeviceType.DRYER,
                "owner-1",
                "Living Room"
        );
        when(deviceRepository.existsByDeviceId("device-1"))
                .thenReturn(false);

        Device savedDevice = Device.builder()
                .id(1L)
                .deviceId("device-1")
                .name("Dryer")
                .type(DeviceType.DRYER)
                .ownerId("owner-1")
                .location("Living Room")
                .status(DeviceStatus.OFFLINE)
                .build();


        when(deviceRepository.save(any(Device.class)))
                .thenReturn(savedDevice);


        var response = deviceService.register(request);

        verify(deviceRepository).save(any(Device.class));

        assertEquals("device-1", response.deviceId());
        assertEquals(DeviceStatus.OFFLINE, response.status());
    }


    @Test
    void shouldThrowExceptionWhenDeviceAlreadyExists(){

        RegisterDeviceRequest request =
                new RegisterDeviceRequest(
                        "device-1",
                        "Dryer",
                        DeviceType.DRYER,
                        "owner-1",
                        "Living Room"
                );

        when(deviceRepository.existsByDeviceId("device-1"))
                .thenReturn(true);


        assertThrows(
                ResponseStatusException.class,
                () -> deviceService.register(request)
        );

        verify(deviceRepository, never()).save(any());
    }


    @Test
    void shouldReturnDeviceByDeviceId() {

        Device device = Device.builder()
                .id(1L)
                .deviceId("device-1")
                .name("Dryer")
                .type(DeviceType.DRYER)
                .ownerId("owner-1")
                .location("Living Room")
                .status(DeviceStatus.ONLINE)
                .build();

        when(deviceRepository.findByDeviceId("device-1"))
                .thenReturn(Optional.of(device));

        DeviceResponse response = deviceService.getByDeviceId("device-1");

        assertEquals("device-1", response.deviceId());
        assertEquals("Dryer", response.name());
        assertEquals(DeviceStatus.ONLINE, response.status());

        verify(deviceRepository).findByDeviceId("device-1");
    }


    @Test
    void shouldThrowNotFoundWhenDeviceDoesNotExist(){
        when(deviceRepository.findByDeviceId("device-1"))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> deviceService.getByDeviceId("device-1")
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Device not found"));

        verify(deviceRepository).findByDeviceId("device-1");

    }

    @Test
    void shouldUpdateDeviceStatus() {

        Device device = Device.builder()
                .id(1L)
                .deviceId("device-1")
                .status(DeviceStatus.OFFLINE)
                .build();

        when(deviceRepository.findByDeviceId("device-1"))
                .thenReturn(Optional.of(device));

        deviceService.updateStatus("device-1", DeviceStatus.ONLINE);

        assertEquals(DeviceStatus.ONLINE, device.getStatus());

        verify(deviceRepository).save(device);
    }

}
