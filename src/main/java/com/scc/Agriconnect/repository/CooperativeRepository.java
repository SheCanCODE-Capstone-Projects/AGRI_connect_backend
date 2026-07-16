package com.scc.Agriconnect.repository;

import com.scc.Agriconnect.entity.Cooperative;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CooperativeRepository extends JpaRepository<Cooperative, Long> {
    List<Cooperative> findByStatus(Cooperative.CooperativeStatus status);
    boolean existsByRegistrationNumber(String registrationNumber);
}