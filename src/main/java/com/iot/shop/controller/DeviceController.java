package com.iot.shop.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.shop.dto.DeviceDto;
import com.iot.shop.enums.DeviceStatus;
import com.iot.shop.service.DeviceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/devices")
public class DeviceController {
	private final DeviceService deviceService;
	
	public DeviceController(DeviceService deviceService) {
		this.deviceService = deviceService;
	}
	
	@PostMapping
	public ResponseEntity<DeviceDto> addDevice(@Valid @RequestBody DeviceDto deviceDto) {
	    if (deviceDto.getStatus() == null) 
	        deviceDto.setStatus(DeviceStatus.READY);
	    
	    if (deviceDto.getTemperature() == 0.0) 
	        deviceDto.setTemperature(-1.0);
	    
		return ResponseEntity.ok(deviceService.addDevice(deviceDto));
	}
	
	@PutMapping("/{pinCode}")
	public ResponseEntity<DeviceDto> updateDevice(@PathVariable Long pinCode, 
			@Valid @RequestBody DeviceDto deviceDto) {
		return ResponseEntity.ok(deviceService.updateDevice(pinCode, deviceDto));
	}
	
	@DeleteMapping("/{pinCode}")
	public ResponseEntity<Void> deleteDevice(@PathVariable Long pinCode) {
		deviceService.deleteDevice(pinCode);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/available-for-sale")
	public ResponseEntity<List<DeviceDto>> getAvailableForSaleDevices() {
		return ResponseEntity.ok(deviceService.getAvailableForSaleDevices());
	}
	
	@PostMapping("/configure/{pinCode}")
	public ResponseEntity<DeviceDto> configureDevice(@PathVariable Long pinCode) {
		return ResponseEntity.ok(deviceService.configureDevice(pinCode));
	}
}