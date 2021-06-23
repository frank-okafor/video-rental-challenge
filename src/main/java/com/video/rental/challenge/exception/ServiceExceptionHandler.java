package com.video.rental.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.video.rental.challenge.DTOs.ServiceResponse;

@ResponseBody
@ControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ServiceResponse nullException(NullPointerException e) {
	return new ServiceResponse(e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ServiceResponse userServiceException(ServiceException e) {
	return new ServiceResponse(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ServiceResponse illegalArgumentException(IllegalArgumentException e) {
	return new ServiceResponse(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ServiceResponse generalException(Exception e) {
	return new ServiceResponse(e.getMessage());
    }

}
