package com.video.rental.challenge.DTOs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoDTO {
    @NotNull
    @NotBlank(message = "movie title must be provided")
    private String videoTitle;
    @NotNull
    @NotBlank(message = "movie type must be provided")
    private String videoType;
    @NotNull
    @NotBlank(message = "movie genre must be provided")
    private String videoGenre;
    private Integer maximumAge;
    private Integer releaseYear;
}
