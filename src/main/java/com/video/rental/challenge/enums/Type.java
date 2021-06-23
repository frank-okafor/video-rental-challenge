package com.video.rental.challenge.enums;

import lombok.Getter;

@Getter
public enum Type {
    REGULAR("regular"), CHILDRENS_MOVIE("childrens_movie"), NEW_RELEASE("new_release");

    Type(final String typeName) {
	this.typeName = typeName;
    }

    private String typeName;
}
