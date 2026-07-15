package com.scc.Agriconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cooperatives")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Cooperative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cooperativeId;

    private String name;
    private String registrationNumber;
    private String province;
    private String district;
    private String sector;
    private String contactInfo;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private CooperativeStatus status = CooperativeStatus.PENDING;

    @OneToOne
    @JoinColumn(name = "president_user_id", unique = true)
    private User president;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum CooperativeStatus { PENDING, APPROVED, REJECTED }
}