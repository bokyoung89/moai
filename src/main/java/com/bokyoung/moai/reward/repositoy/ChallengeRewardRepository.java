package com.bokyoung.moai.reward.repositoy;

import com.bokyoung.moai.reward.domain.MoaiChallengeParticipate;
import com.bokyoung.moai.reward.repositoy.projection.ChallengeRewardProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface ChallengeRewardRepository extends JpaRepository<MoaiChallengeParticipate, Long> {

    @Query(nativeQuery = true, value = "SELECT DATE_FORMAT(cp.updated_at, '%Y-%m-%d') AS date, "
            + "SUM(cp.reward_point) AS amountSum "
            + "FROM challenge_participate cp "
            + "WHERE cp.success = '1' "
            + "AND cp.status = 'PARTICIPATED' "
            + "AND DATE(cp.updated_at) BETWEEN (:startDate) AND (:endDate) "
            + "GROUP BY DATE_FORMAT(cp.updated_at, '%Y-%m-%d')")
    List<ChallengeRewardProjection> findChallengeRewardTotalAmountByUpdatedAt(@Param("startDate") LocalDate startDate,
                                                                              @Param("endDate") LocalDate endDate);

    @Query(nativeQuery = true, value = "SELECT DATE_FORMAT(cp.updated_at, '%Y-%m-%d') AS date, "
            + "SUM(CASE WHEN cp.reward_point IS NOT NULL AND cp.apply_point IS NOT NULL "
            + "THEN cp.reward_point - cp.apply_point ELSE 0 END) AS amountSum "
            + "FROM challenge_participate cp "
            + "WHERE cp.success = '1' "
            + "AND cp.status = 'PARTICIPATED' "
            + "AND DATE(cp.updated_at) BETWEEN (:startDate) AND (:endDate) "
            + "GROUP BY DATE_FORMAT(cp.updated_at, '%Y-%m-%d')")
    List<ChallengeRewardProjection> findChallengeRewardExtraAmountByUpdatedAt(@Param("startDate") LocalDate startDate,
                                                                              @Param("endDate") LocalDate endDate);

}
