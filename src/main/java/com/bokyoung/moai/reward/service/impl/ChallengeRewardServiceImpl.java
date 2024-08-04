package com.bokyoung.moai.reward.service.impl;

import com.bokyoung.moai.reward.repositoy.ChallengeRewardRepository;
import com.bokyoung.moai.reward.repositoy.projection.ChallengeRewardProjection;
import com.bokyoung.moai.reward.service.ChallengeRewardService;
import com.bokyoung.moai.reward.service.dto.ChallengeRewardRateResponseDto;
import com.bokyoung.moai.reward.service.dto.ChallengeRewardRequestDto;
import com.bokyoung.moai.reward.service.dto.ChallengeRewardResponseDto;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChallengeRewardServiceImpl implements ChallengeRewardService {

    private final ChallengeRewardRepository challengeRewardRepository;

    @Override
    public List<ChallengeRewardResponseDto> getChallengeRewardAmountAll(ChallengeRewardRequestDto requestDto) {

        List<ChallengeRewardProjection> rewardProjectionList =
                challengeRewardRepository.findChallengeRewardTotalAmountByUpdatedAt(
                        requestDto.getStartDate(), requestDto.getEndDate());

        return getChallengeRewardResponseDtos(requestDto, rewardProjectionList);
    }

    @Override
    public List<ChallengeRewardResponseDto> getChallengeRewardAmountExtra(ChallengeRewardRequestDto requestDto) {
        List<ChallengeRewardProjection> rewardProjectionList =
                challengeRewardRepository.findChallengeRewardExtraAmountByUpdatedAt(
                        requestDto.getStartDate(), requestDto.getEndDate());

        return getChallengeRewardResponseDtos(requestDto, rewardProjectionList);
    }

    @Override
    public List<ChallengeRewardResponseDto> getChallengeRewardWithPeriod(ChallengeRewardRequestDto requestDto, String category, String type) {
        List<ChallengeRewardProjection> rewardProjectionList;

        // category와 type에 따라 데이터 조회 로직을 다르게 처리
        switch (category) {
            case "challenge":
                switch (type) {
                    case "total":
                        rewardProjectionList = challengeRewardRepository.findChallengeRewardTotalAmountByUpdatedAt(
                                requestDto.getStartDate(), requestDto.getEndDate());
                        break;
                    case "net":
                        rewardProjectionList = challengeRewardRepository.findChallengeRewardExtraAmountByUpdatedAt(
                                requestDto.getStartDate(), requestDto.getEndDate());
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid type: " + type);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid category: " + category);
        }

        return getChallengeRewardResponseDtos(requestDto, rewardProjectionList);
    }

    private List<ChallengeRewardResponseDto> getChallengeRewardResponseDtos(ChallengeRewardRequestDto requestDto,
                                                                            List<ChallengeRewardProjection> rewardProjectionList) {
        Map<Integer, Long> weekAmountMap = new HashMap<>();

        // 조회된 데이터를 주별로 집계
        for (ChallengeRewardProjection projection : rewardProjectionList) {
            LocalDate date = projection.getDate();
            Long amountSum = projection.getAmountSum();
            int weekNumber = (int) ChronoUnit.WEEKS.between(requestDto.getStartDate(), date) + 1;


            if (weekAmountMap.containsKey(weekNumber)) {
                weekAmountMap.put(weekNumber, weekAmountMap.get(weekNumber) + amountSum);
            } else {
                weekAmountMap.put(weekNumber, amountSum);
            }
        }

        long totalWeeks = ChronoUnit.WEEKS.between(requestDto.getStartDate(), requestDto.getEndDate()) + 1;
        List<ChallengeRewardResponseDto> responseDtoList = new ArrayList<>();

        // 주별 총액 누적 및 리스트 생성하여 반환
        for (int week = 1; week <= totalWeeks; week++) {
            Long amountSum = weekAmountMap.getOrDefault(week, 0L);
            ChallengeRewardResponseDto responseDto = ChallengeRewardResponseDto.builder()
                    .week(week)
                    .amountSum(amountSum)
                    .build();
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }
}