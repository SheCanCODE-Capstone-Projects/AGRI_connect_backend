package com.scc.Agriconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cooperatives")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Cooperative {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cooperativeId;

    private String name;
    private String registrationNumber;
    private String province;
    private String district;
    private String sector;
    private String contactInfo;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private CooperativeStatus status;

    @OneToOne
    @JoinColumn(name = "president_user_id", unique = true)
    private User president;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = CooperativeStatus.PENDING;
    }
    public enum CooperativeStatus { PENDING, APPROVED, REJECTED }
}