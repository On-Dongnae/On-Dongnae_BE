package com.semo.group1.on_dongnae.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_suspensions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserSuspension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_suspension_id")
    private Long userSuspensionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "suspended_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime suspendedAt = LocalDateTime.now();
}
