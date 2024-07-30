package com.bokyoung.moai.reward.service.impl;

import com.bokyoung.moai.reward.repositoy.ChallengeRewardRepository;
import com.bokyoung.moai.reward.repositoy.projection.ChallengeRewardProjection;
import com.bokyoung.moai.reward.service.ChallengeRewardService;
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

        Map<Integer, Long> weekAmountMap = new HashMap<>();

        // 조회된 데이터를 주별로 집계
        for (ChallengeRewardProjection projection : rewardProjectionList) {
            LocalDate date = projection.getDate();
            Long totalAmount = projection.getTotalAmount();
            int weekNumber = (int) ChronoUnit.WEEKS.between(requestDto.getStartDate(), date) + 1;


            if (weekAmountMap.containsKey(weekNumber)) {
                weekAmountMap.put(weekNumber, weekAmountMap.get(weekNumber) + totalAmount);
            } else {
                weekAmountMap.put(weekNumber, totalAmount);
            }
        }

        long totalWeeks = ChronoUnit.WEEKS.between(requestDto.getStartDate(), requestDto.getEndDate()) + 1;
        List<ChallengeRewardResponseDto> responseDtoList = new ArrayList<>();

        // 주별 총액 누적
        for (int week = 1; week <= totalWeeks; week++) {
            Long amountSum = weekAmountMap.getOrDefault(week, 0L);
            ChallengeRewardResponseDto responseDto = ChallengeRewardResponseDto.builder()
                    .week(week)
                    .total_amount(amountSum)
                    .build();
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }
}
