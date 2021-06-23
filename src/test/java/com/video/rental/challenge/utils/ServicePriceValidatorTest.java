package com.video.rental.challenge.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map.Entry;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.video.rental.challenge.DTOs.PriceDTO;
import com.video.rental.challenge.DTOs.ServiceResponse;
import com.video.rental.challenge.DTOs.VideoDTO;
import com.video.rental.challenge.helper.utils.ServiceTestHelper;

class ServicePriceValidatorTest {

    @ParameterizedTest
    @CsvSource({ "Van_Helson,true", "o,false" })
    void testIsVideoTitleValid(String paramer, boolean expected) {
	// given
	PriceDTO dto = ServiceTestHelper.makePriceRequest();
	dto.setVideoTitle(paramer);
	// when
	Entry<Boolean, ServiceResponse> response = ServicePriceValidator.isVideoTitleValid().apply(dto);
	// then
	assertThat(response.getKey()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({ "frank,true", "f,false" })
    void testIsUsernameValid(String paramer, boolean expected) {
	// given
	PriceDTO dto = ServiceTestHelper.makePriceRequest();
	dto.setUsername(paramer);
	// when
	Entry<Boolean, ServiceResponse> response = ServicePriceValidator.isUsernameValid().apply(dto);
	// then
	assertThat(response.getKey()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({ "6,true", "0,false" })
    void testIsNumberOfRentalDaysValid(Integer paramer, boolean expected) {
	// given
	PriceDTO dto = ServiceTestHelper.makePriceRequest();
	dto.setNumberOfRentalDays(paramer);
	// when
	Entry<Boolean, ServiceResponse> response = ServicePriceValidator.isNumberOfRentalDaysValid().apply(dto);
	// then
	assertThat(response.getKey()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({ "Hell_boy,true", "0,false" })
    void isMovieTitleValid(String paramer, boolean expected) {
	// given
	VideoDTO dto = ServiceTestHelper.createEditVideoRequest();
	dto.setVideoTitle(paramer);
	// when
	Entry<Boolean, ServiceResponse> response = ServiceVideoValidator.isMovieTitleValid().apply(dto);
	// then
	assertThat(response.getKey()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({ "childrens_movie,true", "thriller,false" })
    void isMovieTypeValid(String paramer, boolean expected) {
	// given
	VideoDTO dto = ServiceTestHelper.createEditVideoRequest();
	dto.setVideoType(paramer.toUpperCase());
	// when
	Entry<Boolean, ServiceResponse> response = ServiceVideoValidator.isMovieTypeValid().apply(dto);
	// then
	assertThat(response.getKey()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({ "action,true", "football,false" })
    void isMovieGenreValid(String paramer, boolean expected) {
	// given
	VideoDTO dto = ServiceTestHelper.createEditVideoRequest();
	dto.setVideoGenre(paramer.toUpperCase());
	// when
	Entry<Boolean, ServiceResponse> response = ServiceVideoValidator.isMovieGenreValid().apply(dto);
	// then
	assertThat(response.getKey()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({ "10,true", "0,false" })
    void isMaximumAgeValid(Integer paramer, boolean expected) {
	// given
	VideoDTO dto = ServiceTestHelper.createEditVideoRequest();
	dto.setMaximumAge(paramer);
	// when
	Entry<Boolean, ServiceResponse> response = ServiceVideoValidator.isMaximumAgeValid().apply(dto);
	// then
	assertThat(response.getKey()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({ "2010,true", "0,false" })
    void isReleaseYearValid(Integer paramer, boolean expected) {
	// given
	VideoDTO dto = ServiceTestHelper.createEditVideoRequest();
	dto.setReleaseYear(paramer);
	// when
	Entry<Boolean, ServiceResponse> response = ServiceVideoValidator.isReleaseYearValid().apply(dto);
	// then
	assertThat(response.getKey()).isEqualTo(expected);
    }

}
