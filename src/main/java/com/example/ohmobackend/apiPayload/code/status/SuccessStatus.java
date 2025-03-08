package com.example.ohmobackend.apiPayload.code.status;

import com.example.ohmobackend.apiPayload.code.BaseCode;
import com.example.ohmobackend.apiPayload.code.ErrorReasonDto;
import com.example.ohmobackend.apiPayload.code.ReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    // 유저 관련 응답
    USER_OK(HttpStatus.OK, "MEMBER_1000", "성공입니다."),
    USER_SIGNUP_OK(HttpStatus.OK, "AUTH2000", "회원 가입이 완료되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDto getReason() {
        return ReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ReasonDto getReasonHttpStatus() {
        return ReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}