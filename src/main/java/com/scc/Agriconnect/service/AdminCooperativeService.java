package com.scc.Agriconnect.service;

import com.scc.Agriconnect.entity.Cooperative;
import com.scc.Agriconnect.repository.CooperativeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCooperativeService {

    private final CooperativeRepository cooperativeRepository;

    public List<Cooperative> getPending() {
        return cooperativeRepository.findByStatus(Cooperative.CooperativeStatus.PENDING);
    }

    public Cooperative approve(Long id) {
        Cooperative coop = getOrThrow(id);
        coop.setStatus(Cooperative.CooperativeStatus.APPROVED);
        return cooperativeRepository.save(coop);
    }

    public Cooperative reject(Long id) {
        Cooperative coop = getOrThrow(id);
        coop.setStatus(Cooperative.CooperativeStatus.REJECTED);
        return cooperativeRepository.save(coop);
    }

    private Cooperative getOrThrow(Long id) {
        return cooperativeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cooperative not found: " + id));
    }
}