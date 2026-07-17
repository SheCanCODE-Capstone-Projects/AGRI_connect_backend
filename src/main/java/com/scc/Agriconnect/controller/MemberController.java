package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.MemberRequest;
import com.scc.Agriconnect.dto.MemberResponse;
import com.scc.Agriconnect.entity.Member;
import com.scc.Agriconnect.mapper.MemberMapper;
import com.scc.Agriconnect.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('PRESIDENT', 'STAFF')")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> create(@Valid @RequestBody MemberRequest request) {
        return ResponseEntity.ok(MemberMapper.toResponse(memberService.create(request)));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> list(
            @RequestParam(required = false) Member.MembershipStatus status,
            @RequestParam(required = false) String name) {

        List<MemberResponse> response = memberService.list(status, name).stream()
                .map(MemberMapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(MemberMapper.toResponse(memberService.getOne(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> update(@PathVariable Long id, @Valid @RequestBody MemberRequest request) {
        return ResponseEntity.ok(MemberMapper.toResponse(memberService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MemberResponse> softDelete(@PathVariable Long id) {
        return ResponseEntity.ok(MemberMapper.toResponse(memberService.softDelete(id)));
    }

    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<MemberResponse> reactivate(@PathVariable Long id) {
        return ResponseEntity.ok(MemberMapper.toResponse(memberService.reactivate(id)));
    }
}