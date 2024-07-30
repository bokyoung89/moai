package com.bokyoung.moai.reward.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChallengeParticipateStatus {

    PARTICIPATED("참여 중"),
    WITHDRAWED("참여 취소"),
    BLOCKED("차단");

    private final String statusName;
}