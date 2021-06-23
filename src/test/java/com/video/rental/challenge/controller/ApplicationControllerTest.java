package com.video.rental.challenge.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.rental.challenge.DTOs.PriceDTO;
import com.video.rental.challenge.DTOs.VideoDTO;
import com.video.rental.challenge.entity.Video;
import com.video.rental.challenge.helper.utils.ServiceTestHelper;
import com.video.rental.challenge.repository.UserRepository;
import com.video.rental.challenge.repository.VideoRepository;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private @Autowired UserRepository userRepo;
    private @Autowired VideoRepository videoRepo;

    @AfterEach
    void tearDown() throws Exception {
	userRepo.deleteAll();
	videoRepo.deleteAll();
    }

    @Test
    void testGetAllVideos() throws Exception {
	videoRepo.saveAll(ServiceTestHelper.videoList());
	mockMvc.perform(get("/video-rentals/api/v1/getallvideos").contentType(MediaType.APPLICATION_JSON)
		.queryParam("pagenumber", "0").queryParam("pagelimit", "6")).andExpect(status().isOk())
		.andExpect(jsonPath("$.message").value("results found!")).andReturn();
    }

    @Test
    void testCreateEditVideo() throws JsonProcessingException, Exception {
	VideoDTO dto = ServiceTestHelper.createEditVideoRequest();
	mockMvc.perform(post("/video-rentals/api/v1/createeditvideo").contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());
	assertThat(videoRepo.findByTitle(dto.getVideoTitle()).isPresent()).isEqualTo(true);
    }

    @Test
    void testGetUserRequestPriceDetails() throws JsonProcessingException, Exception {
	Video video = ServiceTestHelper.videoToCreate();
	PriceDTO dto = ServiceTestHelper.makePriceRequest();
	dto.setVideoTitle(video.getTitle());
	videoRepo.save(video);
	mockMvc.perform(get("/video-rentals/api/v1/getuserrequestpricedetails").contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());
	assertThat(userRepo.findByUsername(dto.getUsername()).isPresent()).isEqualTo(true);
    }

}
