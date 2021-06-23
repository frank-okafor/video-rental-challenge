package com.video.rental.challenge.utils;

import static com.video.rental.challenge.utils.Applicationutils.INVALID_RENTAL_DAYS_VALUE;
import static com.video.rental.challenge.utils.Applicationutils.INVALID_USERNAME;
import static com.video.rental.challenge.utils.Applicationutils.INVALID_VIDEO_TITLE;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.video.rental.challenge.DTOs.PriceDTO;
import com.video.rental.challenge.DTOs.ServiceResponse;

//using the java function combinator design pattern to validate our price request custom fields
public interface ServicePriceValidator extends Function<PriceDTO, Entry<Boolean, ServiceResponse>> {

    static ServicePriceValidator isVideoTitleValid() {
	return price -> price.getVideoTitle().trim().isEmpty() || price.getVideoTitle().length() < 2
		? Map.entry(false, ServiceResponse.builder().message(INVALID_VIDEO_TITLE).data(null).build())
		: Map.entry(true, ServiceResponse.builder().message("").data(null).build());

    }

    static ServicePriceValidator isUsernameValid() {
	return price -> price.getUsername().trim().isEmpty() || price.getUsername().length() < 2
		? Map.entry(false, ServiceResponse.builder().message(INVALID_USERNAME).data(null).build())
		: Map.entry(true, ServiceResponse.builder().message("").data(null).build());

    }

    static ServicePriceValidator isNumberOfRentalDaysValid() {
	return price -> price.getNumberOfRentalDays() <= 0
		? Map.entry(false, ServiceResponse.builder().message(INVALID_RENTAL_DAYS_VALUE).data(null).build())
		: Map.entry(true, ServiceResponse.builder().message("").data(null).build());
    }

    default ServicePriceValidator and(ServicePriceValidator other) {
	return price -> {
	    Map.Entry<Boolean, ServiceResponse> result = this.apply(price);
	    return result.getKey() ? other.apply(price) : result;
	};
    }

}
