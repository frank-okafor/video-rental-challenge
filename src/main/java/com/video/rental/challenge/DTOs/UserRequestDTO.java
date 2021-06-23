package com.video.rental.challenge.DTOs;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDTO {
    private String username;
    private List<ProductDTO> videoList;
    private String totalAmount;

}
