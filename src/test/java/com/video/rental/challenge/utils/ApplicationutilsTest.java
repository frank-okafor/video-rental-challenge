package com.video.rental.challenge.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.video.rental.challenge.DTOs.PriceDTO;
import com.video.rental.challenge.DTOs.UserRequestDTO;
import com.video.rental.challenge.DTOs.VideoDTO;
import com.video.rental.challenge.DTOs.VideoType;
import com.video.rental.challenge.entity.User;
import com.video.rental.challenge.entity.Video;
import com.video.rental.challenge.enums.Type;
import com.video.rental.challenge.helper.utils.ServiceTestHelper;

//testing the application utils layer
class ApplicationutilsTest {

    @ParameterizedTest
    @CsvSource({ "10,85", "20,165" })
    void testCalculateMovieRentalAmount(int parameter, BigDecimal expected) {
	// given
	VideoType type = ServiceTestHelper.getCreatedVideoType();
	// when
	BigDecimal result = Applicationutils.calculateMovieRentalAmount(type, parameter);
	// then
	assertThat(result).isEqualByComparingTo(expected);
    }

    @Test
    void testVideosToSaveOnStartUp() {
	assertThat(Applicationutils.videosToSaveOnStartUp().isEmpty()).isFalse();
    }

    @ParameterizedTest
    @CsvSource({ "regular,true", "testing,false" })
    void testCheckInEnum(String parameter, boolean expected) {
	// when
	boolean result = Applicationutils.checkInEnum(Type.class, parameter.toUpperCase());
	// then
	assertThat(result).isEqualTo(expected);
    }

    @Test
    void convertToUserRequest() {
	// given
	User user = ServiceTestHelper.userToCreate();
	// when
	UserRequestDTO dto = Applicationutils.convertToUserRequest(user);
	// then
	assertNotNull(dto);
	assertThat(dto.getTotalAmount().trim().isEmpty()).isEqualTo(false);
	assertThat(dto.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void newVideo() {
	// given
	VideoDTO dto = ServiceTestHelper.createEditVideoRequest();
	// when
	Video video = Applicationutils.newVideo(dto, null);
	// then
	assertNotNull(dto);
	assertThat(dto.getVideoTitle()).isEqualTo(video.getTitle());
    }

    @Test
    void videoToDto() {
	// given
	Video video = ServiceTestHelper.videoToCreate();
	// when
	VideoDTO dto = Applicationutils.videoToDto(video);
	// then
	assertNotNull(dto);
	assertThat(dto.getVideoTitle()).isEqualTo(video.getTitle());
    }

    @Test
    void getUserPricing() {
	// given
	PriceDTO dto = ServiceTestHelper.makePriceRequest();
	Video video = ServiceTestHelper.videoToCreate();
	// when
	Map.Entry<User, UserRequestDTO> result = Applicationutils.getUserPricing(dto, null, video);
	User user = result.getKey();
	UserRequestDTO request = result.getValue();
	// then
	assertNotNull(user);
	assertNotNull(request);
	assertThat(request.getTotalAmount().trim().isEmpty()).isEqualTo(false);
	assertThat(dto.getUsername()).isEqualTo(user.getUsername());
    }

}
