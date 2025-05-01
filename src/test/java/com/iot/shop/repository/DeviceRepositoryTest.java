package com.iot.shop.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.iot.shop.entity.Device;
import com.iot.shop.enums.DeviceStatus;

@DataJpaTest
@ActiveProfiles("test")
class DeviceRepositoryTest {

	@Autowired
	private DeviceRepository deviceRepository;
    
    @BeforeEach
    void setUp() {
    	deviceRepository.save(new Device(1000001L, DeviceStatus.READY, -1.0));
    	deviceRepository.save(new Device(1000002L, DeviceStatus.ACTIVE, 7.0));
    }
    
    @Test
    void shouldCheckIfPinCodeExisits() {
    	assertThat(deviceRepository.existsByPinCode(1000002L)).isTrue();
    	assertThat(deviceRepository.existsByPinCode(1000003L)).isFalse();
    }
    
    @Test
    void shouldFindDeviceByPinCode() {
    	Optional<Device> foundDevice = deviceRepository.findByPinCode(1000001L);
    	assertThat(foundDevice).isPresent();
    	assertThat(foundDevice.get().getStatus()).isEqualTo(DeviceStatus.READY);
    	assertThat(foundDevice.get().getTemperature()).isEqualTo(-1.0);
    	
    	assertThat(deviceRepository.findByPinCode(1000003L)).isEmpty();
    }
    
    @Test
    void shouldReturnDevicesAvailableForSaleInOrder() {
    	List<Device> activeDevices = deviceRepository.findByStatusOrderByPinCodeAsc(DeviceStatus.ACTIVE);
    	assertThat(activeDevices).hasSize(1);
    	assertThat(activeDevices.get(0).getPinCode()).isEqualTo(1000002L);
    	assertThat(activeDevices.get(0).getTemperature()).isEqualTo(7.0);
    }
}