package com.bokyoung.moai.userverification.repository;

import com.bokyoung.moai.userverification.domain.MoaiUserVerification;
import com.bokyoung.moai.userverification.repository.projection.MoaiUserVerificationProjection;
import com.bokyoung.moai.userverification.repository.projection.MoaiUserVerificationRouteProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MoaiUserVerificationRepository extends JpaRepository<MoaiUserVerification, Long> {

    @Query(nativeQuery = true, value = "SELECT DATE_FORMAT(uv.created_at, '%Y-%m-%d') AS date, COUNT(1) AS count "
        + "FROM user_verification uv "
        + "WHERE DATE(uv.created_at) BETWEEN (:startDate) AND (:endDate) "
        + "GROUP BY DATE_FORMAT(uv.created_at, '%Y-%m-%d')")
    List<MoaiUserVerificationProjection> findUserVerificationCountByDateInGroupByCreatedAt(@Param("startDate") LocalDate startDate,
                                                                                           @Param("endDate") LocalDate endDate);

    @Query(nativeQuery = true, value = "SELECT uil.route AS route, DATE_FORMAT(uv.created_at, '%Y-%m-%d') AS date, COUNT(uv.did) AS count "
            + "FROM user_verification uv "
            + "INNER JOIN user_influx_log uil ON uv.did = uil.did "
            + "WHERE DATE(uv.created_at) BETWEEN (:startDate) AND (:endDate) "
            + "GROUP BY uil.route, DATE_FORMAT(uv.created_at, '%Y-%m-%d')")
    List<MoaiUserVerificationRouteProjection> findUserVerificationCountByRouteInGroupByCreatedAtAndRoute(@Param("startDate") LocalDate startDate,
                                                                                                         @Param("endDate") LocalDate endDate);

}