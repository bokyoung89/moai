package com.bokyoung.moai.newmember.repository;

import com.bokyoung.moai.newmember.domain.Member;
import com.bokyoung.moai.newmember.repository.projection.NewMemberProjection;
import com.bokyoung.moai.newmember.repository.projection.NewMemberRouteProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(nativeQuery = true, value = "SELECT DATE_FORMAT(m.created_at, '%Y-%m-%d') AS date, COUNT(1) AS count "
            + "FROM member m "
            + "WHERE DATE(m.created_at) BETWEEN (:startDate) AND (:endDate) "
            + "GROUP BY DATE_FORMAT(m.created_at, '%Y-%m-%d')")
    List<NewMemberProjection> findNewMemberCountByCreatedAt(@Param("startDate") LocalDate startDate,
                                                            @Param("endDate") LocalDate endDate);

    @Query(nativeQuery = true, value = "SELECT DATE_FORMAT(m.created_at, '%Y-%m-%d') AS date, uil.route AS route, COUNT(m.did) AS count "
            + "FROM member m "
            + "INNER JOIN user_influx_log uil ON m.did = uil.did "
            + "WHERE DATE(m.created_at) BETWEEN (:startDate) AND (:endDate) "
            + "GROUP BY uil.route, DATE_FORMAT(m.created_at, '%Y-%m-%d')")
    List<NewMemberRouteProjection> findNewMemberCountByCreatedAtAndRoute(@Param("startDate") LocalDate startDate,
                                                                         @Param("endDate") LocalDate endDate);

}
