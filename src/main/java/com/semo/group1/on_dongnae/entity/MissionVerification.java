package com.semo.group1.on_dongnae.entity;

import com.semo.group1.on_dongnae.entity.enums.VerificationSource;
import com.semo.group1.on_dongnae.entity.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mission_verifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MissionVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_verification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_mission_id", nullable = false)
    private UserMission userMission;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private VerificationStatus status = VerificationStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private VerificationSource source;

    @Column(name = "ai_confidence", precision = 5, scale = 4)
    private BigDecimal aiConfidence;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @OneToMany(mappedBy = "missionVerification", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<VerificationImage> images = new ArrayList<>();
}
