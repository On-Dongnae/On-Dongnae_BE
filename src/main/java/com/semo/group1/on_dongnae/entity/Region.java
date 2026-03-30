package com.semo.group1.on_dongnae.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "regions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long regionId;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 50)
    private String district;

    @Column(nullable = false, length = 50)
    private String unit;

    @Column(precision = 10, scale = 7)
    private Double latitude;

    @Column(precision = 10, scale = 7)
    private Double longitude;
}
