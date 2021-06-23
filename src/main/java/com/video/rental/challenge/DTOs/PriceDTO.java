package com.video.rental.challenge.DTOs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceDTO {
    @NotNull
    @NotBlank(message = "movie title must be provided")
    private String videoTitle;
    @NotNull
    @NotBlank(message = "username must be provided")
    private String username;
    @NotNull
    private Integer numberOfRentalDays;

}
