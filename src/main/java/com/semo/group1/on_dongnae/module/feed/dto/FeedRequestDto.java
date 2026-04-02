package com.semo.group1.on_dongnae.module.feed.dto;

import com.semo.group1.on_dongnae.entity.enums.FeedGroup;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedRequestDto {
    private FeedGroup type;
    private String title;
    private String content;
}
