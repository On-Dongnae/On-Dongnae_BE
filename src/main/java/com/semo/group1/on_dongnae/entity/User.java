package com.semo.group1.on_dongnae.entity;

import com.semo.group1.on_dongnae.entity.enums.Role;
import com.semo.group1.on_dongnae.entity.enums.UserStatus;
import com.semo.group1.on_dongnae.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @Column(nullable = false, unique = true, length = 225)
    private String email;

    @Column(nullable = false, length = 225)
    private String password;

    @Column(name = "username", nullable = false, length = 50)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "profile_image_url", length = 225)
    private String profileImageUrl;

    @Column(name = "total_score", nullable = false)
    @Builder.Default
    private Integer totalScore = 0;


}
