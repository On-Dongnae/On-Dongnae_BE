package com.semo.group1.on_dongnae.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedSort {
    LATEST("최신순"),
    LIKES("좋아요순");

    private final String description;
}
