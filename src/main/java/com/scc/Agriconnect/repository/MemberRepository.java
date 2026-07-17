package com.scc.Agriconnect.repository;

import com.scc.Agriconnect.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("""
            SELECT m FROM Member m
            WHERE m.cooperative.cooperativeId = :cooperativeId
            AND (:status IS NULL OR m.membershipStatus = :status)
            AND (:name IS NULL OR LOWER(m.fullName) LIKE LOWER(CONCAT('%', :name, '%')))
            ORDER BY m.fullName ASC
            """)
    List<Member> search(
            @Param("cooperativeId") Long cooperativeId,
            @Param("status") Member.MembershipStatus status,
            @Param("name") String name
    );

    Optional<Member> findByMemberIdAndCooperative_CooperativeId(Long memberId, Long cooperativeId);
}