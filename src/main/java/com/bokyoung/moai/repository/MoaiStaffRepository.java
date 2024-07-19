package com.bokyoung.moai.repository;

import com.bokyoung.moai.domain.MoaiStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoaiStaffRepository extends JpaRepository<MoaiStaff, Long> {

    Optional<MoaiStaff> findByUserId(String userId);

    boolean existsByUserId(String userId);
}
