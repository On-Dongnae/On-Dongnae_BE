package com.semo.group1.on_dongnae.module.region.service;

import com.semo.group1.on_dongnae.module.region.dto.RegionResponseDto;
import com.semo.group1.on_dongnae.module.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    @Transactional(readOnly = true)
    public List<RegionResponseDto> getAllRegions() {
        return regionRepository.findAll().stream()
                .map(RegionResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RegionResponseDto> searchRegions(String keyword) {
        return regionRepository.findByCityContainingOrDistrictContaining(keyword, keyword)
                .stream()
                .map(RegionResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
