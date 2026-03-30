package com.semo.group1.on_dongnae.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_images")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VerificationImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_image_id")
    private Long verificationImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_verification_id", nullable = false)
    private MissionVerification missionVerification;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Column(name = "image_order", nullable = false)
    @Builder.Default
    private Integer imageOrder = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
