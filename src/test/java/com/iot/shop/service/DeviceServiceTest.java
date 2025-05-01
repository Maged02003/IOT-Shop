package com.iot.shop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.iot.shop.dto.DeviceDto;
import com.iot.shop.entity.Device;
import com.iot.shop.enums.DeviceStatus;
import com.iot.shop.repository.DeviceRepository;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {
	
	@Mock
	private DeviceRepository deviceRepository;
	
	@InjectMocks
	private DeviceService deviceService;
	
	private DeviceDto deviceDto;
	private Device device;
	
	@BeforeEach
	void setUp() {
		deviceDto = new DeviceDto(1000001L, DeviceStatus.READY, -1.0);
		device = new Device(1000001L, DeviceStatus.READY, -1.0);
		device.setId(1L);
	}
	
	@Test
	void shouldAddDeviceWithValidDto() {
		when(deviceRepository.existsByPinCode(1000001L)).thenReturn(false);
		when(deviceRepository.save(any(Device.class))).thenReturn(device);
		
		DeviceDto result = deviceService.addDevice(deviceDto);
		
		assertThat(result.getPinCode()).isEqualTo(1000001L);
		assertThat(result.getStatus()).isEqualTo(DeviceStatus.READY);
		assertThat(result.getTemperature()).isEqualTo(-1.0);
		verify(deviceRepository).save(any(Device.class));
	}
	
	@Test
	void shouldAddDeviceWithDefaultValuesIfNull() {
		deviceDto.setStatus(null);
		deviceDto.setTemperature(0.0);
		
		when(deviceRepository.existsByPinCode(1000001L)).thenReturn(false);
		when(deviceRepository.save(any(Device.class))).thenReturn(device);
		
		DeviceDto result = deviceService.addDevice(deviceDto);
		
		assertThat(result.getPinCode()).isEqualTo(1000001L);
		assertThat(result.getStatus()).isEqualTo(DeviceStatus.READY);
		assertThat(result.getTemperature()).isEqualTo(-1.0);
		verify(deviceRepository).save(any(Device.class));
	}
	
	@Test
	void shouldThrowExceptionForExistingPinCode() {
		when(deviceRepository.existsByPinCode(1000001L)).thenReturn(true);
		
		IllegalArgumentException iaException = assertThrows(IllegalArgumentException.class, 
				() -> deviceService.addDevice(deviceDto));
		
		assertThat(iaException.getMessage()).isEqualTo("Pin code already exists");
		verify(deviceRepository, never()).save(any(Device.class));
	}
	
	@Test
	void shouldUpdateDevice() {
		DeviceDto updatedDto = new DeviceDto(1000002L, DeviceStatus.READY, -1.0);
		
		Device updatedDevice = new Device(1000002L, DeviceStatus.READY, -1.0);
		updatedDevice.setId(1L);
		
		when(deviceRepository.findByPinCode(1000001L)).thenReturn(Optional.of(device));
		when(deviceRepository.existsByPinCode(1000002L)).thenReturn(false);
		when(deviceRepository.save(any(Device.class))).thenReturn(updatedDevice);
		
		DeviceDto result = deviceService.updateDevice(1000001L, updatedDto);
		
		assertThat(result.getPinCode()).isEqualTo(1000002L);
		assertThat(result.getStatus()).isEqualTo(DeviceStatus.READY);
		assertThat(result.getTemperature()).isEqualTo(-1.0);
		verify(deviceRepository).save(any(Device.class));
	}
	
	@Test
	void shouldThrowExceptionWhenUpdatingNonExistentDevice() {
		when(deviceRepository.findByPinCode(1000001L)).thenReturn(Optional.empty());
		
		IllegalArgumentException iaException = assertThrows(IllegalArgumentException.class,
				() -> deviceService.updateDevice(1000001L, deviceDto));
		
		assertThat(iaException.getMessage()).isEqualTo("Device not found");
		verify(deviceRepository, never()).save(any(Device.class));
	}
	
	@Test
	void shouldThrowExceptionWhenUpdatingToExistingPinCode() {
		DeviceDto updatedDto = new DeviceDto(1000002L, DeviceStatus.READY, -1.0);
		
		when(deviceRepository.findByPinCode(1000001L)).thenReturn(Optional.of(device));
		when(deviceRepository.existsByPinCode(1000002L)).thenReturn(true);
		
		IllegalArgumentException iaException = assertThrows(IllegalArgumentException.class, 
				() -> deviceService.updateDevice(1000001L, updatedDto));
		
		assertThat(iaException.getMessage()).isEqualTo("Pin code already exists");
		verify(deviceRepository, never()).save(any(Device.class));
	}
	
	@Test
	void shouldDeleteDevice() {
		when(deviceRepository.findByPinCode(1000001L)).thenReturn(Optional.of(device));
		
		deviceService.deleteDevice(1000001L);
		
		verify(deviceRepository).delete(device);
	}
	
	@Test
	void shouldThrowExceptionWhenDeletingNonExistentDevice() {
		when(deviceRepository.findByPinCode(1000001L)).thenReturn(Optional.empty());
		
		IllegalArgumentException iaException = assertThrows(IllegalArgumentException.class, 
				() -> deviceService.deleteDevice(1000001L));
		
		assertThat(iaException.getMessage()).isEqualTo("Device not found");
		verify(deviceRepository, never()).delete(any(Device.class));
	}
	
	@Test
	void shouldGetAvailableForSaleDevices() {
		Device activeDevice = new Device(1000001L, DeviceStatus.ACTIVE, 5.0);
		activeDevice.setId(1L);
		
		when(deviceRepository.findByStatusOrderByPinCodeAsc(DeviceStatus.ACTIVE))
				.thenReturn(List.of(activeDevice));
		
		List<DeviceDto> result = deviceService.getAvailableForSaleDevices();
		
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getPinCode()).isEqualTo(1000001L);
		assertThat(result.get(0).getStatus()).isEqualTo(DeviceStatus.ACTIVE);
		assertThat(result.get(0).getTemperature()).isEqualTo(5.0);
		
		verify(deviceRepository).findByStatusOrderByPinCodeAsc(DeviceStatus.ACTIVE);
	}
	
	@Test
	void shouldConfigure() {
		when(deviceRepository.findByPinCode(1000001L)).thenReturn(Optional.of(device));
		when(deviceRepository.save(any(Device.class))).thenAnswer(invocation -> {
			Device savedDevice = invocation.getArgument(0);
			savedDevice.setId(1L);
			return savedDevice;
		});
		
		DeviceDto result = deviceService.configureDevice(1000001L);
		
		assertThat(result.getPinCode()).isEqualTo(1000001L);
		assertThat(result.getStatus()).isEqualTo(DeviceStatus.ACTIVE);
		assertThat(result.getTemperature()).isBetween(0.0, 10.0);
		verify(deviceRepository).save(any(Device.class));
	}
	
	@Test
	void shouldThrowExceptionWhenConfiguringNonExistentDevice() {
		when(deviceRepository.findByPinCode(1000001L)).thenReturn(Optional.empty());
		
		IllegalArgumentException iaException = assertThrows(IllegalArgumentException.class,
				() -> deviceService.configureDevice(1000001L));
		
		assertThat(iaException.getMessage()).isEqualTo("Device not found");
		verify(deviceRepository, never()).save(any(Device.class));
	}
}