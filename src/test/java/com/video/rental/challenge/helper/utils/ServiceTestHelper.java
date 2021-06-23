package com.video.rental.challenge.helper.utils;

import static com.video.rental.challenge.enums.Type.CHILDRENS_MOVIE;
import static com.video.rental.challenge.enums.Type.NEW_RELEASE;
import static com.video.rental.challenge.enums.Type.REGULAR;
import static com.video.rental.challenge.enums.VideoGenre.ACTION;
import static com.video.rental.challenge.enums.VideoGenre.COMEDY;
import static com.video.rental.challenge.enums.VideoGenre.DRAMA;
import static com.video.rental.challenge.enums.VideoGenre.HORROR;
import static com.video.rental.challenge.enums.VideoGenre.ROMANCE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.github.javafaker.Faker;
import com.video.rental.challenge.DTOs.PriceDTO;
import com.video.rental.challenge.DTOs.VideoDTO;
import com.video.rental.challenge.DTOs.VideoType;
import com.video.rental.challenge.entity.Products;
import com.video.rental.challenge.entity.User;
import com.video.rental.challenge.entity.Video;

public class ServiceTestHelper {
    static final Faker faker = Faker.instance();

    public static List<Video> videoList() {
	return Arrays.asList(new Video("Scent of a Woman", new VideoType(REGULAR, 1992, null), DRAMA),
		new Video("Wonder Woman", new VideoType(REGULAR, 2017, null), ACTION),
		new Video("Curious George", new VideoType(CHILDRENS_MOVIE, 2006, 12), COMEDY),
		new Video("The Longest Ride", new VideoType(REGULAR, 2015, null), ROMANCE),
		new Video("A Quiet Place Part II", new VideoType(NEW_RELEASE, 2021, null), HORROR),
		new Video("Jackass 4", new VideoType(NEW_RELEASE, 2021, null), COMEDY));
    }

    public static Video videoToCreate() {
	return new Video(faker.name().title(), new VideoType(REGULAR, 2017, null), ACTION);
    }

    public static List<Products> productList(User user) {
	return Arrays.asList(new Products(faker.name().title(), 4, new BigDecimal("20"), user),
		new Products(faker.name().title(), 5, new BigDecimal("30"), user),
		new Products(faker.name().title(), 2, new BigDecimal("40"), user));

    }

    public static User userToCreate() {
	User user = new User("frank", new ArrayList<>());
	user.setProducts(productList(user));
	return user;
    }

    public static Page<Video> videoPages() {
	Page<Video> page = new PageImpl<>(videoList());
	return page;
    }

    public static PriceDTO makePriceRequest() {
	return PriceDTO.builder().numberOfRentalDays(10).username(faker.name().username())
		.videoTitle(faker.name().title()).build();
    }

    public static VideoDTO createEditVideoRequest() {
	return VideoDTO.builder().maximumAge(10).releaseYear(2009).videoGenre(ACTION.getGenreName())
		.videoTitle(faker.name().title()).videoType(REGULAR.getTypeName()).build();
    }

    public static VideoType getCreatedVideoType() {
	return new VideoType(CHILDRENS_MOVIE, 2006, 10);
    }

}
