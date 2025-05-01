package com.iot.shop.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iot.shop.dto.DeviceDto;
import com.iot.shop.entity.Device;
import com.iot.shop.enums.DeviceStatus;
import com.iot.shop.repository.DeviceRepository;

@Service
public class DeviceService {
	private final DeviceRepository deviceRepository;
	
	public DeviceService(DeviceRepository deviceRepository) {
		this.deviceRepository = deviceRepository;
	}
	
	@Transactional
	public DeviceDto addDevice(DeviceDto deviceDto) {
		if(deviceRepository.existsByPinCode(deviceDto.getPinCode())) 
			throw new IllegalArgumentException("Pin code already exists");
		
        DeviceStatus status = deviceDto.getStatus() != null ? deviceDto.getStatus() : DeviceStatus.READY;
        double temperature = deviceDto.getTemperature() != 0.0 ? deviceDto.getTemperature() : -1.0;

		Device device = new Device(deviceDto.getPinCode(), status, temperature);
		
		return convertToDto(deviceRepository.save(device));
	}
	
	@Transactional
	public DeviceDto updateDevice(Long pinCode, DeviceDto deviceDto) {
		Device device = deviceRepository.findByPinCode(pinCode)
				.orElseThrow(() -> new IllegalArgumentException("Device not found"));
		
		if(!device.getPinCode().equals(deviceDto.getPinCode()) 
				&& deviceRepository.existsByPinCode(deviceDto.getPinCode()))
			throw new IllegalArgumentException("Pin code already exists");
		
		device.setPinCode(deviceDto.getPinCode());
		device.setStatus(deviceDto.getStatus());
		device.setTemperature(deviceDto.getTemperature());
		
		return convertToDto(deviceRepository.save(device));
	}
	
	@Transactional
	public void deleteDevice(Long pinCode) {
		Device device = deviceRepository.findByPinCode(pinCode)
				.orElseThrow(() -> new IllegalArgumentException("Device not found"));
		deviceRepository.delete(device);
	}
	
	public List<DeviceDto> getAvailableForSaleDevices() {
		return deviceRepository.findByStatusOrderByPinCodeAsc(DeviceStatus.ACTIVE)
				.stream().map(this::convertToDto).collect(Collectors.toList());
	}
	
	@Transactional
	public DeviceDto configureDevice(Long pinCode) {
		Device device = deviceRepository.findByPinCode(pinCode)
				.orElseThrow(() -> new IllegalArgumentException("Device not found"));
		
		Random random = new Random();
		device.setStatus(DeviceStatus.ACTIVE);
		device.setTemperature(random.nextDouble() * 10);
		return convertToDto(deviceRepository.save(device));
	}
	
	private DeviceDto convertToDto(Device device) {
		DeviceDto deviceDto = new DeviceDto();
		deviceDto.setPinCode(device.getPinCode());
		deviceDto.setStatus(device.getStatus());
		deviceDto.setTemperature(device.getTemperature());
		return deviceDto;
	}
}