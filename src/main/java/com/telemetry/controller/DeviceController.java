package com.telemetry.controller;


import com.telemetry.dto.DeviceResponse;
import com.telemetry.dto.RegisterDeviceRequest;
import com.telemetry.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.swing.event.ListDataEvent;
import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    //register
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceResponse register(@Valid @RequestBody RegisterDeviceRequest request){
        return deviceService.register(request);
    }

    @GetMapping
    public List<DeviceResponse> listAll(){
        return deviceService.listAll();
    }

    @GetMapping("/owner/{ownerId}")
    public List<DeviceResponse> listByOwner(@PathVariable String ownerId){
        return deviceService.listByOwner(ownerId);
    }

    @GetMapping("/{deviceId}")
    public DeviceResponse getByDeviceId(@PathVariable String deviceId){
        return deviceService.getByDeviceId(deviceId);
    }


}
