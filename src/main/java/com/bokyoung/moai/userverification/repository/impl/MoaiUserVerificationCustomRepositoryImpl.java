//package com.bokyoung.moai.userverification.repository.impl;
//
//import com.bokyoung.moai.userverification.domain.MoaiUserVerification;
//import com.bokyoung.moai.userverification.repository.MoaiUserVerificationCustomRepository;
//import com.bokyoung.moai.userverification.repository.projection.MoaiUserVerificationProjection;
//import com.bokyoung.moai.userverification.repository.projection.MoaiUserVerificationRouteProjection;
//import com.querydsl.core.types.Projections;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Repository
//public class MoaiUserVerificationCustomRepositoryImpl extends QuerydslRepositorySupport implements MoaiUserVerificationCustomRepository {
//
//    private final JPAQueryFactory jpaQueryFactory;
//
//    public MoaiUserVerificationCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
//        super(MoaiUserVerification.class);
//        this.jpaQueryFactory = jpaQueryFactory;
//    }
//
//    @Override
//    public List<MoaiUserVerificationRouteProjection> findUserVerificationCountByRouteInGroupByCreatedAtandRoute(LocalDate startDate, LocalDate endDate) {
//        return jpaQueryFactory.select(Projections.constructor(MoaiUserVerificationProjection.class,
//                userInfluxLog.route,
//                userVerification.createdAt.stringValue().substring(0, 10).as("day"),
//                userVerification.did.count().as("count")
//                )).from(userVerification)
//                .innerJoin(userInfluxLog).on(userVerification.did.eq(userInfluxLog.did))
//                .where(userVerification.createdAt.between(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay()))
//                .groupBy(userInfluxLog.route, userVerification.createdAt.yearMonthDay())
//                .fetch();
//    }
//}
