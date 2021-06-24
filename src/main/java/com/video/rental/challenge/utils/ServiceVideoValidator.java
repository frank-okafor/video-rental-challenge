package com.video.rental.challenge.utils;

import static com.video.rental.challenge.utils.Applicationutils.INVALID_MAXIMUM_AGE;
import static com.video.rental.challenge.utils.Applicationutils.INVALID_RELAESE_YEAR;
import static com.video.rental.challenge.utils.Applicationutils.INVALID_VIDEO_GENRE;
import static com.video.rental.challenge.utils.Applicationutils.INVALID_VIDEO_TITLE;
import static com.video.rental.challenge.utils.Applicationutils.INVALID_VIDEO_TYPE;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.google.common.collect.Maps;
import com.video.rental.challenge.DTOs.ServiceResponse;
import com.video.rental.challenge.DTOs.VideoDTO;
import com.video.rental.challenge.enums.Type;
import com.video.rental.challenge.enums.VideoGenre;

//using the java function combinator design pattern to validate our video custom fields
public interface ServiceVideoValidator extends Function<VideoDTO, Entry<Boolean, ServiceResponse>> {

    static ServiceVideoValidator isMovieTitleValid() {
	return video -> video.getVideoTitle().trim().isEmpty() || video.getVideoTitle().length() < 2
		? Maps.immutableEntry(false, ServiceResponse.builder().message(INVALID_VIDEO_TITLE).data(null).build())
		: Maps.immutableEntry(true, ServiceResponse.builder().message("").data(null).build());

    }

    static ServiceVideoValidator isMovieTypeValid() {
	return video -> !Applicationutils.checkInEnum(Type.class, video.getVideoType().toUpperCase())
		? Maps.immutableEntry(false, ServiceResponse.builder().message(INVALID_VIDEO_TYPE).data(null).build())
		: Maps.immutableEntry(true, ServiceResponse.builder().message("").data(null).build());
    }

    static ServiceVideoValidator isMovieGenreValid() {
	return video -> !Applicationutils.checkInEnum(VideoGenre.class, video.getVideoGenre().toUpperCase())
		? Maps.immutableEntry(false, ServiceResponse.builder().message(INVALID_VIDEO_GENRE).data(null).build())
		: Maps.immutableEntry(true, ServiceResponse.builder().message("").data(null).build());

    }

    static ServiceVideoValidator isMaximumAgeValid() {
	return video -> video.getMaximumAge() <= 0
		? Maps.immutableEntry(false, ServiceResponse.builder().message(INVALID_MAXIMUM_AGE).data(null).build())
		: Maps.immutableEntry(true, ServiceResponse.builder().message("").data(null).build());
    }

    static ServiceVideoValidator isReleaseYearValid() {
	return video -> video.getReleaseYear() <= 0
		? Maps.immutableEntry(false, ServiceResponse.builder().message(INVALID_RELAESE_YEAR).data(null).build())
		: Maps.immutableEntry(true, ServiceResponse.builder().message("").data(null).build());
    }

    default ServiceVideoValidator and(ServiceVideoValidator other) {
	return video -> {
	    Map.Entry<Boolean, ServiceResponse> result = this.apply(video);
	    return result.getKey() ? other.apply(video) : result;
	};
    }

}
