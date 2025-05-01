package com.iot.shop.repository.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.shop.dto.DeviceDto;
import com.iot.shop.enums.DeviceStatus;
import com.iot.shop.service.DeviceService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(DeviceControllerTest.TestConfig.class)
class DeviceControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private DeviceService deviceService;
	
	@TestConfiguration
	static class TestConfig {
		@Bean
		public DeviceService deviceService () {
			return Mockito.mock(DeviceService.class);
		}
	}

	@Test
	void shouldAddDevice() throws Exception {
		DeviceDto deviceDto = new DeviceDto(1000001L, DeviceStatus.READY, -1.0);
		
		when(deviceService.addDevice(any(DeviceDto.class))).thenReturn(deviceDto);
		
		
		mockMvc.perform(post("/devices").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(deviceDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.pinCode").value(1000001L))
				.andExpect(jsonPath("$.status").value("READY"))
				.andExpect(jsonPath("$.temperature").value(-1.0));
	}
	
	@Test
	void shouldAddDeviceWithNullStatusAndTemperature() throws Exception {
		DeviceDto inputDto = new DeviceDto(1000001L, null, 0.0);
		DeviceDto responseDto = new DeviceDto(1000001L, DeviceStatus.READY, -1.0);
		
		when(deviceService.addDevice(any(DeviceDto.class))).thenReturn(responseDto);
		
		mockMvc.perform(post("/devices").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(inputDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.pinCode").value(1000001L))
				.andExpect(jsonPath("$.status").value("READY"))
				.andExpect(jsonPath("$.temperature").value(-1.0));
	}
	
	@Test
	void shouldUpdateDevice() throws Exception {
		DeviceDto deviceDto = new DeviceDto(1000001L, DeviceStatus.READY, -1.0);
		
		when(deviceService.updateDevice(eq(1000001L), any(DeviceDto.class))).thenReturn(deviceDto);
		
		mockMvc.perform(put("/devices/1000001").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(deviceDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.pinCode").value(1000001L));
	}
	
	@Test
	void shouldDeleteDevice() throws Exception {
		mockMvc.perform(delete("/devices/1000001")).andExpect(status().isOk());
	}
	
	
	@Test
	void shouldGetAvailableForSaleDevices() throws Exception {
		DeviceDto deviceDto = new DeviceDto(1000001L, DeviceStatus.ACTIVE, 5.0);
		
		when(deviceService.getAvailableForSaleDevices()).thenReturn(List.of(deviceDto));
       
		mockMvc.perform(get("/devices/available-for-sale"))
        	.andExpect(status().isOk())
        	.andExpect(jsonPath("$[0].pinCode").value(1000001L))
        	.andExpect(jsonPath("$[0].status").value("ACTIVE"));
	}
	
	@Test
	void shouldConfigureDevice() throws Exception {
		DeviceDto deviceDto = new DeviceDto(1000001L, DeviceStatus.ACTIVE, 5.0);
		
		when(deviceService.configureDevice(1000001L)).thenReturn(deviceDto);
		
		mockMvc.perform(post("/devices/configure/1000001")).andExpect(status().isOk())
			.andExpect(jsonPath("$.pinCode").value(1000001L))
			.andExpect(jsonPath("$.status").value("ACTIVE"));
	}
}