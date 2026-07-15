package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.CooperativeResponse;
import com.scc.Agriconnect.mapper.CooperativeMapper;
import com.scc.Agriconnect.service.AdminCooperativeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/cooperatives")
@RequiredArgsConstructor
public class AdminCooperativeController {

    private final AdminCooperativeService adminCooperativeService;

    @GetMapping("/pending")
    public ResponseEntity<List<CooperativeResponse>> getPending() {
        var response = adminCooperativeService.getPending().stream()
                .map(CooperativeMapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<CooperativeResponse> approve(@PathVariable Long id) {
        return ResponseEntity.ok(CooperativeMapper.toResponse(adminCooperativeService.approve(id)));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<CooperativeResponse> reject(@PathVariable Long id) {
        return ResponseEntity.ok(CooperativeMapper.toResponse(adminCooperativeService.reject(id)));
    }
}