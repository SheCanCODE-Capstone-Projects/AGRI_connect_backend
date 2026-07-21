package com.scc.Agriconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "staff_invitations")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class StaffInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID invitationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cooperative_id", nullable = false)
    private Cooperative cooperative;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by_user_id", nullable = false)
    private User invitedBy;

    @Builder.Default
    @Column(nullable = false, unique = true)
    private String token = UUID.randomUUID().toString();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private InvitationStatus status = InvitationStatus.PENDING;

    @CreationTimestamp
    private LocalDateTime invitedAt;

    private LocalDateTime acceptedAt;

    public enum InvitationStatus { PENDING, ACCEPTED, EXPIRED }
}