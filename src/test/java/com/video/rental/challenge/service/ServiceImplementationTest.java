package com.video.rental.challenge.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import com.video.rental.challenge.DTOs.PriceDTO;
import com.video.rental.challenge.DTOs.ServiceResponse;
import com.video.rental.challenge.DTOs.VideoDTO;
import com.video.rental.challenge.entity.User;
import com.video.rental.challenge.entity.Video;
import com.video.rental.challenge.exception.ServiceException;
import com.video.rental.challenge.helper.utils.ServiceTestHelper;
import com.video.rental.challenge.repository.ProductRepository;
import com.video.rental.challenge.repository.UserRepository;
import com.video.rental.challenge.repository.VideoRepository;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class ServiceImplementationTest {
    @Mock
    private UserRepository userRepo;
    @Mock
    private VideoRepository videoRepo;
    @Mock
    private ProductRepository productRepo;
    @Mock
    private Pageable pageableMock;
    private ServiceImplementation underTest;

    @BeforeEach
    void setUp() throws Exception {
	underTest = new ServiceImplementation(userRepo, videoRepo, productRepo);
    }

    @Test
    void testGetAllVideos() {
	// when
	when(videoRepo.findAll(any(Pageable.class))).thenReturn(ServiceTestHelper.videoPages());
	Entry<Boolean, ServiceResponse> response = underTest.getAllVideos(0, 4);
	@SuppressWarnings("unchecked")
	List<VideoDTO> result = (List<VideoDTO>) response.getValue().getData();
	// then
	assertThat(result.isEmpty()).isEqualTo(false);
	assertThat(response.getKey()).isEqualTo(true);
    }

    @Test
    void testCreateVideo() {
	// given
	VideoDTO dto = ServiceTestHelper.createEditVideoRequest();
	// when
	when(videoRepo.findByTitle(anyString())).thenReturn(Optional.ofNullable(ServiceTestHelper.videoToCreate()));
	Entry<Boolean, ServiceResponse> response = underTest.createEditVideo(dto);
	// then
	assertThat(dto).isEqualTo(response.getValue().getData());
	verify(videoRepo).save(any(Video.class));
	assertThat(response.getKey()).isEqualTo(true);
    }

    @Test
    void testEditVideo() {
	// given
	VideoDTO dto = ServiceTestHelper.createEditVideoRequest();
	// when
	when(videoRepo.findByTitle(anyString())).thenReturn(Optional.empty());
	Entry<Boolean, ServiceResponse> response = underTest.createEditVideo(dto);
	// then
	assertThat(dto).isEqualTo(response.getValue().getData());
	verify(videoRepo).save(any(Video.class));
	assertThat(response.getKey()).isEqualTo(true);
    }

    @Test
    void testPostRequestWhenTitleDoesNotExist() {
	// given
	PriceDTO dto = ServiceTestHelper.makePriceRequest();
	// when
	when(videoRepo.findByTitle(anyString())).thenReturn(Optional.empty());
	// then
	assertThatThrownBy(() -> underTest.getUserRequestPriceDetails(dto)).isInstanceOf(ServiceException.class)
		.hasMessageContaining("video with title '" + dto.getVideoTitle()
			+ "' does not exist, however you can create it" + " and add to your collection");
	verify(videoRepo, never()).save(any());
    }

    @Test
    void testGetUserRequestPriceDetails() {
	// given
	PriceDTO dto = ServiceTestHelper.makePriceRequest();
	// when
	when(videoRepo.findByTitle(anyString())).thenReturn(Optional.ofNullable(ServiceTestHelper.videoToCreate()));
	when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());
	Entry<Boolean, ServiceResponse> response = underTest.getUserRequestPriceDetails(dto);
	// then
	verify(userRepo).save(any(User.class));
	assertThat(response.getKey()).isEqualTo(true);
	assertNotNull(response.getValue().getData());
    }

}
