package com.video.rental.challenge.utils;

import static com.video.rental.challenge.enums.Type.CHILDRENS_MOVIE;
import static com.video.rental.challenge.enums.Type.NEW_RELEASE;
import static com.video.rental.challenge.enums.Type.REGULAR;
import static com.video.rental.challenge.enums.VideoGenre.ACTION;
import static com.video.rental.challenge.enums.VideoGenre.COMEDY;
import static com.video.rental.challenge.enums.VideoGenre.DRAMA;
import static com.video.rental.challenge.enums.VideoGenre.HORROR;
import static com.video.rental.challenge.enums.VideoGenre.ROMANCE;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.video.rental.challenge.DTOs.PriceDTO;
import com.video.rental.challenge.DTOs.ProductDTO;
import com.video.rental.challenge.DTOs.UserRequestDTO;
import com.video.rental.challenge.DTOs.VideoDTO;
import com.video.rental.challenge.DTOs.VideoType;
import com.video.rental.challenge.entity.Products;
import com.video.rental.challenge.entity.User;
import com.video.rental.challenge.entity.Video;
import com.video.rental.challenge.enums.Type;
import com.video.rental.challenge.enums.VideoGenre;

@Component
public class Applicationutils {
    private static final int REGULAR_PRICE_PER_DAY = 10;
    private static final int CHILDREN_MOVIE_PRICE_PER_DAY = 8;
    private static final int NEW_RELEASE_PRICE_PER_DAY = 15;
    public static final String INVALID_VIDEO_TITLE = "video title is invalid, please input a valid title";
    public static final String INVALID_VIDEO_GENRE = "video genre is invalid";
    public static final String INVALID_VIDEO_TYPE = "video type is invalid";
    public static final String INVALID_MAXIMUM_AGE = "maximum age is invalid, please input a valid age in integers greater than 0";
    public static final String INVALID_RELAESE_YEAR = "release year is invalid, please input a valid year in integers greater than 0";
    public static final String INVALID_USERNAME = "username is invalid";
    public static final String INVALID_RENTAL_DAYS_VALUE = "username is invalid, please input a valid number of days in integers greater than 0";

    // custom method to calculate pricing for video types
    public static BigDecimal calculateMovieRentalAmount(VideoType type, int numberOfDays) {
	BigDecimal finalBalance = new BigDecimal("0.00");
	switch (type.getType()) {
	case CHILDRENS_MOVIE: {
	    int amount = (CHILDREN_MOVIE_PRICE_PER_DAY * numberOfDays) + (type.getMaximumAge() / 2);
	    finalBalance = new BigDecimal(amount);
	}
	    break;
	case NEW_RELEASE: {
	    int amount = (NEW_RELEASE_PRICE_PER_DAY * numberOfDays) - type.getReleaseYear();
	    finalBalance = new BigDecimal(amount);
	}
	    break;
	case REGULAR: {
	    int amount = (REGULAR_PRICE_PER_DAY * numberOfDays);
	    finalBalance = new BigDecimal(amount);
	}
	    break;
	default: {
	    finalBalance = new BigDecimal("0.00");
	}
	}
	return finalBalance;
    }

    // create custom videos to be saved on application start-up for live tests
    public static List<Video> videosToSaveOnStartUp() {
	return Arrays.asList(new Video("Scent of a Woman", new VideoType(REGULAR, 1992, null), DRAMA),
		new Video("Wonder Woman", new VideoType(REGULAR, 2017, null), ACTION),
		new Video("Curious George", new VideoType(CHILDRENS_MOVIE, 2006, 12), COMEDY),
		new Video("The Longest Ride", new VideoType(REGULAR, 2015, null), ROMANCE),
		new Video("A Quiet Place Part II", new VideoType(NEW_RELEASE, 2021, null), HORROR),
		new Video("Jackass 4", new VideoType(NEW_RELEASE, 2021, null), COMEDY));
    }

    // check if provided string is an instance of enum class
    public static <E extends Enum<E>> Boolean checkInEnum(Class<E> enumClass, String name) {
	if (StringUtils.isBlank(name)) {
	    return false;
	}
	try {
	    E e = Enum.valueOf(enumClass, name);
	    return true;
	} catch (Exception e) {
	    return false;
	}
    }

    // convert transfer object to video
    public static Video newVideo(VideoDTO dto, Video video) {
	VideoType type = VideoType.builder().maximumAge(dto.getMaximumAge()).releaseYear(dto.getReleaseYear())
		.type(Type.valueOf(dto.getVideoType().toUpperCase())).build();
	if (video != null) {
	    video.setGenre(VideoGenre.valueOf(dto.getVideoGenre().toUpperCase()));
	    video.setTitle(dto.getVideoTitle());
	    video.setType(type);
	    return video;
	}
	return new Video(dto.getVideoTitle(), type, VideoGenre.valueOf(dto.getVideoGenre().toUpperCase()));
    }

    // convert video to transfer object
    public static VideoDTO videoToDto(Video video) {
	return VideoDTO.builder().maximumAge(video.getType().getMaximumAge()).videoTitle(video.getTitle())
		.videoGenre(video.getGenre().getGenreName()).releaseYear(video.getType().getReleaseYear())
		.videoType(video.getType().getType().getTypeName()).build();
    }

    // create or update new user request object
    public static Entry<User, UserRequestDTO> getUserPricing(PriceDTO dto, User user, Video video) {
	Products product = Products.builder().videoTitle(video.getTitle())
		.numberOfRentalDays(dto.getNumberOfRentalDays())
		.totalAmount(calculateMovieRentalAmount(video.getType(), dto.getNumberOfRentalDays())).build();
	if (user != null) {
	    product.setUser(user);
	    user.getProducts().add(product);
	    return Maps.immutableEntry(user, convertToUserRequest(user));
	}
	User newUser = User.builder().username(dto.getUsername()).products(new ArrayList<>()).build();
	product.setUser(newUser);
	List<Products> newProductList = Arrays.asList(product);
	newUser.setProducts(newProductList);
	return Maps.immutableEntry(newUser, convertToUserRequest(newUser));
    }

    // get total prices for member requests and convert to user request object
    public static UserRequestDTO convertToUserRequest(User user) {
	DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
	BigDecimal totalAmount = user.getProducts().parallelStream().map(u -> u.getTotalAmount())
		.reduce(BigDecimal.ZERO, BigDecimal::add);
	return UserRequestDTO.builder().username(user.getUsername())
		.videoList(user.getProducts().parallelStream()
			.map(product -> ProductDTO.builder().numberOfRentalDays(product.getNumberOfRentalDays())
				.videoTitle(product.getVideoTitle()).amount(product.getTotalAmount()).build())
			.collect(Collectors.toList()))
		.totalAmount("USD" + df.format(totalAmount)).build();
    }

}
