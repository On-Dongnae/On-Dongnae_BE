package com.semo.group1.on_dongnae.entity;

import com.semo.group1.on_dongnae.entity.enums.FeedGroup;
import com.semo.group1.on_dongnae.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "feeds")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "feed_group", nullable = false)
    private FeedGroup type;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "like_count", nullable = false)
    @Builder.Default
    private Integer likeCount = 0;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FeedImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public void update(String title, String content, FeedGroup type) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (type != null) this.type = type;
    }

    public void plusLike() {
        this.likeCount++;
    }

    public void minusLike() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }
}
