package com.video.rental.challenge.enums;

import lombok.Getter;

@Getter
public enum VideoGenre {
    ACTION("action"), DRAMA("drama"), ROMANCE("romance"), COMEDY("comedy"), HORROR("horror");

    VideoGenre(final String genreName) {
	this.genreName = genreName;
    }

    private String genreName;

}
