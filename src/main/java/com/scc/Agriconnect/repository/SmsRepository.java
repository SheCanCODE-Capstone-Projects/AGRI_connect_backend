package com.scc.Agriconnect.repository;

import com.scc.Agriconnect.entity.Sms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SmsRepository extends JpaRepository<Sms, UUID> {
    Page<Sms> findByCooperative_CooperativeIdOrderByCreatedAtDesc(UUID cooperativeId, Pageable pageable);
}
