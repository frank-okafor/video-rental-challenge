package com.video.rental.challenge.DTOs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.video.rental.challenge.enums.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoType {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;
    @Column(name = "release_year")
    private Integer releaseYear;
    @Column(name = "maximum_age")
    private Integer maximumAge;

}
