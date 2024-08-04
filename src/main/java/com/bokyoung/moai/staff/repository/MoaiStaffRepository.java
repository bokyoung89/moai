package com.bokyoung.moai.staff.repository;

import com.bokyoung.moai.staff.domain.MoaiStaff;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoaiStaffRepository extends JpaRepository<MoaiStaff, Long> {

    Optional<MoaiStaff> findByUserId(String userId);

    boolean existsByUserId(String userId);
}
