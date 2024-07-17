package com.bokyoung.moai.exception.domain;

import com.bokyoung.moai.exception.MydServerException;

import java.util.Arrays;

/**
 * <h1>ErrorCode.java</h1>
 *
 * @author yeongmin Sim
 * @version 1.0
 * @note https://confluence.snplab.io/display/CBU/Error+Codes
 * @since 2022-10-05
 */
public enum ErrorCode {

    // BLOCKCHAIN
    ERR_EXPIRED_PROPOSAL(1004, "expired proposal"),
    ERR_INACTIVATE_PROPOSAL(1009, "inactive proposal"),
    ERR_CANNOT_ISSUE_TICKET(1501, "cannot issue ticket"),
    ERR_ALREADY_REVOKED_TICKET(1503, "already revoked ticket"),
    ERR_ALREADY_ISSUED_BY_DID(1507, "already issued by did"),
    ERR_NO_DATA_FOUND(8004, "no data found"),
    ERR_NOT_VALID_DATE_PROPOSAL(1015, "not valid date proposal"),
    ERR_ALREADY_ISSUED_BY_MOBILE_ID(1016, "already issued by mobile id"),
    ERR_NOT_VALID_AFTER_PROPOSAL(1008, "not valid after proposal"), // deprecated
    ERR_NOT_VALID_BEFORE_PROPOSAL(1007, "not valid before proposal"), // deprecated
    ERR_EXPIRED_TICKET(1504, "expired ticket"),
    ERR_CANNOT_CONSUME_BY_PERIOD(1508, "can not consume by period"),
    ERR_BEFORE_VALIDAT_TICKET(1509, "before validat ticket"),
    ERR_REFUND_TICKET(1597, "refunded ticket"),
    ERR_DROUGHT_TICKET(1598, "drought ticket"),
    ERR_DROUGHT_PROPOSAL(1098, "drought proposal"),
    ERR_NOT_CLAIMABLE_YET(2010, "not claimable yet"),
    ERR_NOT_FOR_YOUR_TICKET(2023, "not for your ticket"),
    ERR_GET_STATE_FAILED(8001, "get state failed"),
    ERR_NO_DATE_FOUND(8004, "no data found"),
    ERR_NOT_SUPPORTED(9003, "not supported"),
    ERR_INVALID_ARGUMENT(9004, "invalid argument"),
    SUCCESS(0, "Success"),

    // MIDDLEWARE
    ERR_UNKNOWN(3000, "알 수 없는 오류가 발생했습니다."),
    ERR_UNREGISTER_QUESTION(3001, "등록되지 않은 질문입니다."),
    ERR_ALREADY_REGISTER_ANSWER(3002, "이미 등록된 답변입니다."),
    ERR_ANSWER_LIMIT_EXCEEDED(3003, "답변 제한이 초과되었습니다."),
    ERR_COMPLETE_REWARD(3004, "보상이 정상적으로 수행되지 않았습니다."),
    ERR_SAVE_ANSWER_STASTICS(3005, "답변 및 통계정보 저장 시 오류가 발생했습니다."),
    ERR_REMOVE_ANSWER_STASTICS(3006, "답변 및 통계 정보 삭제 시 오류가 발생했습니다."),
    ERR_UNMATCH_OWNER(3021, "DID and Owner are not identical."),
    ERR_NOT_FOUND_DATA(3022, "데이터를 찾을 수 없습니다."),
    ERR_STORED_ELASTIC(3023, "Elastic Error Message : "),
    ERR_STORED_ELASTIC_EACH(3024, "Faileure Count : "),
    ERR_AUTHORIZED(3025, "Authorized Error : "),
    ERR_STORED_DATABASE(3026, "데이터 저장에 실패하였습니다."),
    ERR_NOT_ACTIVATE(3031, "활성화 할 수 없습니다."),
    ERR_NOT_DELETE(3032, "삭제 할 수 없습니다."),
    ERR_NOTICE_COUNT_EXCEED(3033, "최대 활성화 수가 초과되었습니다."),
    ERR_EXCEED_NOTAFTER(3034, "현재 시간이 표출 일자를 초과하였습니다."),
    ERR_NOT_BETWEEN_INVISIBLEPERIOD(3035, "보지 않기 유형을 노출 일자에 맞게 고쳐주세요."),
    ERR_EVENT_NOTICE_URL_FORMAT(3036, "랜딩 URL형태가 일치하지 않습니다."),
    ERR_NOT_UPDATE(3037, "수정 할 수 없습니다."),
    ERR_PASSWORD_NOT_CHANGED(3038, "변경하려는 비밀번호가 기존의 비밀번호와 같습니다."),
    ERR_DATA_DUPLICATED(3039, "이미 등록된 데이터입니다. 중복되지 않는 데이터를 입력해주세요."),
    ERR_OBJECT_MAX_COUNT_EXCEED(3040, "최대 활성화 수가 초과하였습니다."),
    ERR_INVALID_PARAMETER_FORMAT(6001, "invalid paramter format"),
    ERR_INVALID_PARAMETER(6002, "invalid parameter"),

    ERR_INVALID_TOKEN(6003, "invalid token"),
    ERR_EXPIRED_TOKEN(6004, "expired token"),
    ERR_URL_FORMAT(6005, "URL 형식이 맞지 않습니다."),
    ERR_INPUT_PARAMETER(6006, "입력한 정보가 잘못되었습니다."),
    ERR_INVALID_DATETIME(6060, "챌린지 인증 날짜 오류."),
    ERR_FILE_UPLOAD(6101, "파일 업로드에 실패하였습니다."),
    ERR_FILE_IS_EMPTY(6102, "File is empty"),
    ERR_NO_FILE_FOUND(6103, "File is not exist"),
    ERR_NOT_REGISTER(6201, "제안서를 생성할 수 없습니다."),
    ERR_PARSING_COMMON(6301, "데이터 파싱에 실패하였습니다."),
    ERR_PARSING_JSON(6302, "JSON 파싱에 실패하였습니다."),
    ERR_DISABLED(6303, "비활성화 되어있습니다."),
    ERR_POINT_NOT_REQUEST(6304, "포인트를 입력하세요"),
    ERR_INPUT_NOT_EXIST(6305, "필수로 입력해야 하는 값이 없습니다."),
    ERR_INACTIVATE_ADVERTISE(6306, "유효하지 않은 거래 프로포절 입니다."),

    ERR_REWARD_HISTORY_UNIQUE(6307, "중복 호출로 인한 reward_transactions_history 입력 오류입니다."),

    ERR_NOT_FOUND_DATABASE(6308, "DB에서 데이터를 조회할 수 없습니다."),
    ERR_NOT_FOUND_OBJECT(6309, "대상을 찾을 수 없습니다."),

    // On-premise
    ERR_AUTHORIZED_ONPREMISE(7800, "On-premise Authorized Error : "),
    ERR_WRONG_OWNER(7801, "The Owner is Wrong"),
    ERR_ALREADY_EXISTS_OBJECT(7804, "대상이 이미 존재합니다."),
    ERR_NOT_FOUND_PI_DATA(7810, "개인정보 업로드 조회 결과가 없습니다."),

    ERR_INVALID_DATA(3090, "invalid data to decrypt"),
    ERR_NOT_ACCEPT(4060, "Not Accept");


    private int code;

    private String description;

    private ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ErrorCode fromCode(int code) {
        return Arrays.stream(values())
            .filter(errorCode -> errorCode.getCode() == code)
            .findFirst()
            .orElseThrow(() -> new MydServerException(ERR_NOT_FOUND_OBJECT, "에러 코드를 찾을 수 없습니다."));
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
