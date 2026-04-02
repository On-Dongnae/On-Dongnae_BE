package com.semo.group1.on_dongnae.module.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private Long feedId;
    private String content;
}
