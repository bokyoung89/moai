package com.bokyoung.moai.staff.exception;

import com.bokyoung.moai.staff.exception.domain.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MydArgumentException extends MydBaseException {
    // 오류 메시지를 직접 정의해서 전달할 경우 사용
    public MydArgumentException(ErrorCode code, String message) {
        super(code, message);
    }

    // 오류 메시지를 ErrCode에 정의된 description을 활용할 경우
    public MydArgumentException(ErrorCode code) {
        super(code, code.getDescription());
    }
}
