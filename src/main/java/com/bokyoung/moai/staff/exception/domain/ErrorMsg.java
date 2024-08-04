package com.bokyoung.moai.staff.exception.domain;

import com.bokyoung.moai.staff.exception.MydBaseException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMsg {

    private String message;
    private int code;

    public static ErrorMsg failedInstance(HttpStatus status, String message) {
        return ErrorMsg.builder()
                .code(status.value())
                .message(StringUtils.trimToNull(message) == null ? status.getReasonPhrase() : message)
                .build();
    }

    public static ErrorMsg failedInstance(ErrorCode errorCode, String message) {
        return ErrorMsg.builder()
                .code(errorCode.getCode())
                .message(StringUtils.trimToNull(message) == null ? errorCode.getDescription() : message)
                .build();
    }

    public static ErrorMsg failedInstance(MydBaseException e){
        return ErrorMsg.builder()
                .code(e.getErrorCode().getCode())
                .message(StringUtils.trimToNull(e.getMessage()) == null ? e.getErrorCode().getDescription() : e.getMessage())
                .build();
    }
}
