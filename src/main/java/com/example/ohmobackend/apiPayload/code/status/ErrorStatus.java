package com.example.ohmobackend.apiPayload.code.status;

import com.example.ohmobackend.apiPayload.code.BaseErrorCode;
import com.example.ohmobackend.apiPayload.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 유저 관련 응답
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자를 찾을 수 없습니다."),
    MEMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "MEMBER4002", "이미 존재하는 유저입니다."),

    // 인증 관련
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "AUTH4001", "유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "AUTH4002", "만료된 토큰입니다."),

    // 카테고리 관련
    MEMBER_CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER_CATEGORY4001", "카레고리를 찾을 수 없습니다."),
    INVALID_MEMBER_CATEGORY(HttpStatus.BAD_REQUEST, "MEMBER_CATEGORY4002", "사용자의 카테고리가 아닙니다."),
    MEMBER_CATEGORY_NOT_ROUTINE_TYPE(HttpStatus.BAD_REQUEST, "MEMBER_CATEGORY4003", "카테고리가 루틴이 아닙니다."),
    MEMBER_CATEGORY_NOT_TO_DO_TYPE(HttpStatus.BAD_REQUEST, "MEMBER_CATEGORY4004", "카테고리가 투두가 아닙니다."),

    // 일정 관련
    MISSING_TIME(HttpStatus.BAD_REQUEST, "SCHEDULE4001", "시간이 누락되었습니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.BAD_REQUEST, "SCHEDULE4002", "일정을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
