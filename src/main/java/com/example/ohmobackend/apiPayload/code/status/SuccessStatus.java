package com.example.ohmobackend.apiPayload.code.status;

import com.example.ohmobackend.apiPayload.code.BaseCode;
import com.example.ohmobackend.apiPayload.code.ReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    // 유저 관련 응답
    MEMBER_OK(HttpStatus.OK, "MEMBER_1000", "성공입니다."),
    MEMBER_SIGNUP_OK(HttpStatus.OK, "AUTH2000", "회원 가입이 완료되었습니다."),
    MEMBER_LOGIN_OK(HttpStatus.OK, "AUTH2001", "로그인이 완료되었습니다."),

    // 카텍고리 관련 응답
    MEMBER_CATEGORY_REGISTER_OK(HttpStatus.OK, "CATEGORY2000", "카테고리 등록이 완료되었습니다."),
    MEMBER_CATEGORY_OK(HttpStatus.OK, "CATEGORY2001", "카테고리 조회가 완료되었습니다."),

    // 스케줄 관련 응답
    SCHEDULE_ROUTINE_OK(HttpStatus.OK, "SCHEDULE2000", "루틴 일정이 등록이 완료되었습니다"),
    SCHEDULE_TO_DO_OK(HttpStatus.OK, "SCHEDULE2001", "투두 일정이 등록이 완료되었습니다"),
    SCHEDULE_UPDATE_STATUS_OK(HttpStatus.OK, "SCHEDULE2002", "스케줄 상태 변경이 완료되었습니다."),
    SCHEDULE_OK(HttpStatus.OK, "SCHEDULE2003", "스케줄 조회가 완료되었습니다.");

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