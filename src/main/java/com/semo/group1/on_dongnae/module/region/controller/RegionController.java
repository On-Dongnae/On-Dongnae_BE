package com.semo.group1.on_dongnae.module.region.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.region.dto.RegionResponseDto;
import com.semo.group1.on_dongnae.module.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RegionResponseDto>>> getAllRegions() {
        List<RegionResponseDto> response = regionService.getAllRegions();
        return ResponseEntity.ok(ApiResponse.ok("전체 동네 목록 조회 성공", response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<RegionResponseDto>>> searchRegions(@RequestParam("keyword") String keyword) {
        List<RegionResponseDto> response = regionService.searchRegions(keyword);
        return ResponseEntity.ok(ApiResponse.ok("동네 검색 성공", response));
    }
}
