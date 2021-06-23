package com.video.rental.challenge.DTOs;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {
    private String videoTitle;
    private int numberOfRentalDays;
    private BigDecimal amount;

}
