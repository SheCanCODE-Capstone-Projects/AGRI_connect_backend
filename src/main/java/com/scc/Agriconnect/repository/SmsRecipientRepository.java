package com.scc.Agriconnect.repository;

import com.scc.Agriconnect.entity.SmsRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SmsRecipientRepository extends JpaRepository<SmsRecipient, UUID> {
    List<SmsRecipient> findBySms_SmsId(UUID smsId);
}
