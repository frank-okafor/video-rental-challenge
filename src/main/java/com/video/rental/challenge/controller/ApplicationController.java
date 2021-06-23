package com.video.rental.challenge.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.video.rental.challenge.DTOs.PriceDTO;
import com.video.rental.challenge.DTOs.ServiceResponse;
import com.video.rental.challenge.DTOs.VideoDTO;
import com.video.rental.challenge.exception.ServiceException;
import com.video.rental.challenge.service.ServiceImplementation;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/video-rentals/api/v1/")
@AllArgsConstructor
public class ApplicationController {
    @Autowired
    private ServiceImplementation service;

    @GetMapping("getallvideos")
    @ApiOperation(value = "get all videos in the database")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "successful", response = ServiceResponse.class),
	    @ApiResponse(code = 500, message = "internal server errors - critical!", response = ServiceResponse.class) })
    public ResponseEntity<?> getAllVideos(
	    @RequestParam(value = "pagenumber", required = true, defaultValue = "1") Integer pageNumber,
	    @RequestParam(value = "pagelimit", required = true, defaultValue = "10") Integer pageLimit)
	    throws Exception {
	Map.Entry<Boolean, ServiceResponse> result = service.getAllVideos(pageNumber, pageLimit);
	return ResponseEntity.status(result.getKey() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(result.getValue());
    }

    @PostMapping("createeditvideo")
    @ApiOperation(value = "create a new video or edit existing one")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "successful", response = ServiceResponse.class),
	    @ApiResponse(code = 500, message = "internal server errors - critical!", response = ServiceResponse.class) })
    public ResponseEntity<?> createEditVideo(@Valid @RequestBody VideoDTO dto) throws Exception {
	Map.Entry<Boolean, ServiceResponse> result = service.createEditVideo(dto);
	return ResponseEntity.status(result.getKey() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(result.getValue());
    }

    @GetMapping("getuserrequestpricedetails")
    @ApiOperation(value = "apply for video request and get pricing values")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "successful", response = ServiceResponse.class),
	    @ApiResponse(code = 401, message = "record not found", response = ServiceException.class),
	    @ApiResponse(code = 500, message = "internal server errors - critical!", response = ServiceResponse.class) })
    public ResponseEntity<?> getUserRequestPriceDetails(@Valid @RequestBody PriceDTO dto) throws Exception {
	Map.Entry<Boolean, ServiceResponse> result = service.getUserRequestPriceDetails(dto);
	return ResponseEntity.status(result.getKey() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(result.getValue());
    }

}
