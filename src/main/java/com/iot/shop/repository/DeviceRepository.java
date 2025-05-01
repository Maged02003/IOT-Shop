package com.iot.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iot.shop.entity.Device;
import com.iot.shop.enums.DeviceStatus;

public interface DeviceRepository extends JpaRepository<Device, Long> {
	boolean existsByPinCode(Long pinCode);
	Optional<Device> findByPinCode(Long pinCode);
	List<Device> findByStatusOrderByPinCodeAsc(DeviceStatus status);
}
