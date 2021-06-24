package com.video.rental.challenge.service;

import static com.video.rental.challenge.utils.ServicePriceValidator.isNumberOfRentalDaysValid;
import static com.video.rental.challenge.utils.ServicePriceValidator.isUsernameValid;
import static com.video.rental.challenge.utils.ServicePriceValidator.isVideoTitleValid;
import static com.video.rental.challenge.utils.ServiceVideoValidator.isMaximumAgeValid;
import static com.video.rental.challenge.utils.ServiceVideoValidator.isMovieGenreValid;
import static com.video.rental.challenge.utils.ServiceVideoValidator.isMovieTitleValid;
import static com.video.rental.challenge.utils.ServiceVideoValidator.isMovieTypeValid;
import static com.video.rental.challenge.utils.ServiceVideoValidator.isReleaseYearValid;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.video.rental.challenge.DTOs.PriceDTO;
import com.video.rental.challenge.DTOs.ServiceResponse;
import com.video.rental.challenge.DTOs.UserRequestDTO;
import com.video.rental.challenge.DTOs.VideoDTO;
import com.video.rental.challenge.entity.User;
import com.video.rental.challenge.entity.Video;
import com.video.rental.challenge.enums.Type;
import com.video.rental.challenge.exception.ServiceException;
import com.video.rental.challenge.repository.ProductRepository;
import com.video.rental.challenge.repository.UserRepository;
import com.video.rental.challenge.repository.VideoRepository;
import com.video.rental.challenge.utils.Applicationutils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ServiceImplementation {
    private @Autowired UserRepository userRepo;
    private @Autowired VideoRepository videoRepo;
    private @Autowired ProductRepository productRepo;

    // save some videos to the database on application start-up
    @PostConstruct
    private void init() {
	videoRepo.saveAll(Applicationutils.videosToSaveOnStartUp());
    }

    // get all available videos in the database
    public Entry<Boolean, ServiceResponse> getAllVideos(int pageNumber, int pageLimit) {
	if (pageNumber > 0) {
	    pageNumber -= 1;
	}
	Pageable request = PageRequest.of(pageNumber, pageLimit);
	Page<Video> videoPages = videoRepo.findAll(request);
	List<Video> videos = videoPages.getContent();
	List<VideoDTO> response = videos.parallelStream().map(video -> Applicationutils.videoToDto(video))
		.collect(Collectors.toList());
	return Maps.immutableEntry(true, ServiceResponse.builder().message("results found!").data(response).build());
    }

    // create a new video or edit already existing
    public Entry<Boolean, ServiceResponse> createEditVideo(VideoDTO dto) {
	// check that all fields for creating or editing video are valid
	Entry<Boolean, ServiceResponse> response = isMovieGenreValid().and(isMovieTitleValid()).and(isMovieTypeValid())
		.apply(dto);
	if (!response.getKey()) {
	    return response;
	}
	// validate for children type movies
	if (dto.getVideoType().equalsIgnoreCase(Type.CHILDRENS_MOVIE.getTypeName())) {
	    response = isMaximumAgeValid().apply(dto);
	    if (!response.getKey()) {
		return response;
	    }
	}
	// validate for new releases
	if (dto.getVideoType().equalsIgnoreCase(Type.NEW_RELEASE.getTypeName())) {
	    response = isReleaseYearValid().apply(dto);
	    if (!response.getKey()) {
		return response;
	    }
	}
	Optional<Video> optionalVideo = videoRepo.findByTitle(dto.getVideoTitle());
	if (optionalVideo.isPresent()) {
	    Video video = optionalVideo.get();
	    videoRepo.save(Applicationutils.newVideo(dto, video));
	    response = Maps.immutableEntry(true,
		    ServiceResponse.builder().message("video updated successfully").data(dto).build());
	} else {
	    response = saveVideoIfNotExists(dto);
	}
	return response;
    }

    // get price details for user requests.. create new if user doesn't already
    // exist or add to the request of already existing users
    public Entry<Boolean, ServiceResponse> getUserRequestPriceDetails(PriceDTO dto) {
	Entry<Boolean, ServiceResponse> response = isVideoTitleValid().and(isNumberOfRentalDaysValid())
		.and(isUsernameValid()).apply(dto);
	if (!response.getKey()) {
	    return response;
	}
	if (!videoRepo.findByTitle(dto.getVideoTitle()).isPresent()) {
	    throw new ServiceException(404, "video with title '" + dto.getVideoTitle()
		    + "' does not exist, however you can create it" + " and add to your collection");
	}
	Video video = videoRepo.findByTitle(dto.getVideoTitle()).get();
	Optional<User> optionalUser = userRepo.findByUsername(dto.getUsername());
	if (optionalUser.isPresent()) {
	    User user = optionalUser.get();
	    Map.Entry<User, UserRequestDTO> result = Applicationutils.getUserPricing(dto, user, video);
	    productRepo.saveAll(result.getKey().getProducts());
	    response = Maps.immutableEntry(true,
		    ServiceResponse.builder().message("request updated successfully").data(result.getValue()).build());
	} else {
	    response = saveUserRequestIfNotExists(dto, video);
	}
	return response;
    }

    // save new video to the db
    private Entry<Boolean, ServiceResponse> saveVideoIfNotExists(VideoDTO dto) {
	videoRepo.save(Applicationutils.newVideo(dto, null));
	return Maps.immutableEntry(true,
		ServiceResponse.builder().message("video created successfully").data(dto).build());
    }

    // save new user request to the db
    private Entry<Boolean, ServiceResponse> saveUserRequestIfNotExists(PriceDTO dto, Video video) {
	Map.Entry<User, UserRequestDTO> result = Applicationutils.getUserPricing(dto, null, video);
	userRepo.save(result.getKey());
	return Maps.immutableEntry(true,
		ServiceResponse.builder().message("request created successfully").data(result.getValue()).build());
    }
}
