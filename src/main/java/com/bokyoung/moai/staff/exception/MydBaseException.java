package com.bokyoung.moai.staff.exception;

import com.bokyoung.moai.staff.exception.domain.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MydBaseException extends RuntimeException {
    private ErrorCode errorCode;
    private String message;

    public MydBaseException(ErrorCode code, String message) {
        this.errorCode = code;
        this.message = message;
    }
}
