package com.video.rental.challenge.DTOs;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponse {
	private String message;
	private String timestamp;
	private Object data;

	public ServiceResponse(String message) {
		this.message = message;
		this.timestamp = LocalDateTime.now().toString();
	}
}
