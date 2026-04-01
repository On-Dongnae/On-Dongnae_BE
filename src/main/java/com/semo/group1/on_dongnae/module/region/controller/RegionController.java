package com.semo.group1.on_dongnae.module.region.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.region.dto.RegionDto;
import com.semo.group1.on_dongnae.module.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    // 1. 지역 목록 전체 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<RegionDto>>> getAllRegions() {
        List<RegionDto> regions = regionService.getAllRegions();
        return ResponseEntity.ok(ApiResponse.ok("지역 목록을 성공적으로 불러왔습니다.", regions));
    }

    // 2. 구 이름(district) 기반 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<RegionDto>>> searchRegions(
            @RequestParam(name = "keyword", required = true) String keyword) {

        List<RegionDto> regions = regionService.searchRegions(keyword);
        return ResponseEntity.ok(ApiResponse.ok("동네 검색을 성공적으로 완료했습니다.", regions));
    }
}
