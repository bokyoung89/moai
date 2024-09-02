package com.bokyoung.moai.newmember.repository;

import com.bokyoung.moai.newmember.domain.Member;
import com.bokyoung.moai.newmember.repository.projection.NewMemberProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(nativeQuery = true, value = "SELECT DATE_FORMAT(m.created_at, '%Y-%m-%d') AS date, mp.gender AS gender, mp.age_range AS ageRange "
            + "FROM member m "
            + "LEFT JOIN member_persona mp ON m.did = mp.did "
            + "WHERE DATE(m.created_at) BETWEEN (:startDate) AND (:endDate) ")
    List<NewMemberProjection> findNewMemberCountByCreatedAt(@Param("startDate") LocalDate startDate,
                                                            @Param("endDate") LocalDate endDate);
}
