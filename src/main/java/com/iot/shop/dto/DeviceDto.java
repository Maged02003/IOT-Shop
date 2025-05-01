package com.iot.shop.dto;

import com.iot.shop.enums.DeviceStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DeviceDto {
	@NotNull
	@Min(1000000)
	@Max(9999999)
	private Long pinCode;
	
	private DeviceStatus status;
	
	@Min(-1)
	@Max(10)
	private double temperature;

	public DeviceDto() { super(); }

	public DeviceDto(@NotNull @Min(1000000) @Max(9999999) Long pinCode, DeviceStatus status,
			@Min(-1) @Max(10) double temperature) {
		super();
		this.pinCode = pinCode;
		this.status = status;
		this.temperature = temperature;
	}

	public Long getPinCode() { return pinCode; }
	public void setPinCode(Long pinCode) { this.pinCode = pinCode; }
	public DeviceStatus getStatus() { return status; }
	public void setStatus(DeviceStatus status) { this.status = status; }
	public double getTemperature() { return temperature; }
	public void setTemperature(double temperature) { this.temperature = temperature; }
}