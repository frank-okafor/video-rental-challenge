package com.video.rental.challenge.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.video.rental.challenge.DTOs.VideoType;
import com.video.rental.challenge.enums.VideoGenre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Data
@Table(name = "video")
public class Video extends BaseEntity {
    @Column(nullable = false, name = "title")
    private String title;
    @Column(nullable = false)
    private VideoType type;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoGenre genre;

}
