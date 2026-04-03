package com.semo.group1.on_dongnae.module.region.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.region.dto.RegionResponseDto;
import com.semo.group1.on_dongnae.module.region.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Region (지역)", description = "동네 검색 및 정보 조회")
@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @Operation(summary = "전체 동네 목록 조회", description = "시스템에 등록된 모든 동네 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RegionResponseDto>>> getAllRegions() {
        List<RegionResponseDto> response = regionService.getAllRegions();
        return ResponseEntity.ok(ApiResponse.ok("전체 동네 목록 조회 성공", response));
    }

    @Operation(summary = "동네 검색", description = "키워드를 통해 행정구역(동네)을 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<RegionResponseDto>>> searchRegions(@Parameter(description = "검색할 동네 이름 키워드") @RequestParam("keyword") String keyword) {
        List<RegionResponseDto> response = regionService.searchRegions(keyword);
        return ResponseEntity.ok(ApiResponse.ok("동네 검색 성공", response));
    }
}
