package com.scc.Agriconnect.service;

import com.scc.Agriconnect.entity.Cooperative;
import com.scc.Agriconnect.integration.EmailService;
import com.scc.Agriconnect.repository.CooperativeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCooperativeService {

    private final CooperativeRepository cooperativeRepository;
    private final EmailService emailService;

    public List<Cooperative> getPending() {
        return cooperativeRepository.findByStatus(Cooperative.CooperativeStatus.PENDING);
    }

    public Cooperative approve(Long id) {
        Cooperative coop = getOrThrow(id);
        coop.setStatus(Cooperative.CooperativeStatus.APPROVED);
        Cooperative saved = cooperativeRepository.save(coop);

        try{
            emailService.sendApprovalNotification(
                    saved.getPresident().getEmail(),
                    saved.getPresident().getFullName(),
                    saved.getName());
        } catch (Exception e) {
            System.err.println("Failed to send approval notification email: "+ e.getMessage());
        }
        return saved;
    }

    public Cooperative reject(Long id) {
        Cooperative coop = getOrThrow(id);
        coop.setStatus(Cooperative.CooperativeStatus.REJECTED);
        Cooperative saved = cooperativeRepository.save(coop);

        try{
            emailService.sendApprovalNotification(
                    saved.getPresident().getEmail(),
                    saved.getPresident().getFullName(),
                    saved.getName());
        } catch (Exception e) {
            System.err.println("Failed to send Rejection notification email: "+ e.getMessage());
        }
        return saved;
    }

    private Cooperative getOrThrow(Long id) {
        return cooperativeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cooperative not found: " + id));
    }
}