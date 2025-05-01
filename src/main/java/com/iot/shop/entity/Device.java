package com.iot.shop.entity;

import com.iot.shop.enums.DeviceStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "devices")
public class Device {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Min(1000000)
	@Max(9999999)
	@Column(name = "pin_code", nullable = false, unique = true)
	private Long pinCode;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private DeviceStatus status;
	
	@Column(name = "temperature", nullable = false)
	private double temperature;

	public Device() {}
	
	public Device(Long pinCode, DeviceStatus status, double temperature) {
		super();
		this.pinCode = pinCode;
		this.status = status;
		this.temperature = temperature;
	}
		
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getPinCode() { return pinCode; }
	public void setPinCode(Long pinCode) { this.pinCode = pinCode; }
	public DeviceStatus getStatus() { return status; }
	public void setStatus(DeviceStatus status) { this.status = status; }
	public double getTemperature() { return temperature; }
	public void setTemperature(double temperature) { this.temperature = temperature; }
}