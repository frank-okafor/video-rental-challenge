package com.video.rental.challenge.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private Integer statusCode;

	public ServiceException(Integer statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}

}
