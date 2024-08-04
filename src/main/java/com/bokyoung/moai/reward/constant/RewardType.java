package com.bokyoung.moai.reward.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RewardType {

    DATATRANSACTION("데이터 마켓"),
    ATTENDANCE("출석 체크"),
    VOTE("오늘의 질문"),
    SURVEY("설문 조사");

    private String value;

    RewardType (String value){
        this.value = value;
    }
}
