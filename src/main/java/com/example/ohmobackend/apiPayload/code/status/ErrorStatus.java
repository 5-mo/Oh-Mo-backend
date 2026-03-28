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
    MEMBER_EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "MEMBER4003", "이미 존재하는 이메일입니다."),
    INVALID_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER4004", "권한이 없습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER4005", "비밀번호가 틀렸습니다."),

    // 인증 관련
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "AUTH4001", "유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "AUTH4002", "만료된 토큰입니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "AUTH4003", "인증 코드가 올바르지 않습니다."),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH5001", "이메일 발송에 실패했습니다."),

    // 카테고리 관련
    MEMBER_CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER_CATEGORY4001", "카레고리를 찾을 수 없습니다."),
    INVALID_MEMBER_CATEGORY(HttpStatus.BAD_REQUEST, "MEMBER_CATEGORY4002", "사용자의 카테고리가 아닙니다."),
    MEMBER_CATEGORY_NOT_ROUTINE_TYPE(HttpStatus.BAD_REQUEST, "MEMBER_CATEGORY4003", "카테고리가 루틴이 아닙니다."),
    MEMBER_CATEGORY_NOT_TO_DO_TYPE(HttpStatus.BAD_REQUEST, "MEMBER_CATEGORY4004", "카테고리가 투두가 아닙니다."),
    MEMBER_CATEGORY_INVALID_SCHEDULE_TYPE(HttpStatus.BAD_REQUEST, "MEMBER_CATEGORY4005", "카테고리가 타입이 불일치 합니다."),
    MEMBER_CATEGORY_ROUTINE_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER_CATEGORY4006", "등록된 루틴이 없습니다."),
    DEFAULT_MEMBER_CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER_CATEGORY4007", "디폴트 카테고리가 없습니다."),

    // 일정 관련
    MISSING_TIME(HttpStatus.BAD_REQUEST, "SCHEDULE4001", "시간이 누락되었습니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.BAD_REQUEST, "SCHEDULE4002", "일정을 찾을 수 없습니다."),
    SCHEDULE_NOT_EXIST(HttpStatus.BAD_REQUEST, "SCHEDULE4003", "일정이 없습니다."),
    SCHEDULE_NOT_TO_DO_TYPE(HttpStatus.BAD_REQUEST, "SCHEDULE4004", "일정이 투두가 아닙니다."),
    SCHEDULE_INVALID_ALARM_TIME(HttpStatus.BAD_REQUEST, "SCHEDULE4005", "알람 시간 등록이 불가능한 일정입니다."),
    SCHEDULE_NOT_GROUP_TYPE(HttpStatus.BAD_REQUEST, "SCHEDULE4006", "그룹 일정이 아닙니다."),
    NLP_PARSE_FAILED(HttpStatus.BAD_REQUEST, "SCHEDULE4007", "텍스트 파싱 실패"),
    NLP_SERVER_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "SCHEDULE5001", "AI 서버 응답 시간이 초과되었습니다. 잠시 후 다시 시도해주세요."),
    NLP_SERVER_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "SCHEDULE5002", "AI 서버를 현재 사용할 수 없습니다. 잠시 후 다시 시도해주세요."),

    // 질문 관련
    QUESTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "QUESTION4001", "등록된 질문이 없습니다."),

    // 일기 관련
    DIARY_NOT_FOUND(HttpStatus.BAD_REQUEST, "DIARY4001", "등록된 일기가 없습니다."),

    // 그룹 관련
    GROUP_NOT_FOUND(HttpStatus.BAD_REQUEST, "GROUP4001", "그룹을 찾을 수 없습니다."),
    GROUP_INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "GROUP4002", "그룹 비밀번호가 틀렸습니다."),
    MEMBER_GROUP_NOT_FOUND(HttpStatus.BAD_REQUEST, "GROUP4002", "그룹에 가입되어 있지 않습니다."),
    GROUP_EXISTS_MEMBER(HttpStatus.BAD_REQUEST, "GROUP4002", "그룹에 가입되어 있습니다."),
    GROUP_NICKNAME_DUPLICATED(HttpStatus.BAD_REQUEST, "GROUP4003", "이미 사용중인 닉네임입니다."),
    GROUP_MEMBER_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "GROUP4004", "그룹 최대 인원을 초과했습니다."),
    GROUP_MANAGER_CANNOT_LEAVE(HttpStatus.BAD_REQUEST, "GROUP4005", "그룹 매니저는 다른 멤버가 있을 경우 그룹을 나갈 수 없습니다."),
    GROUP_NOT_MANAGER(HttpStatus.BAD_REQUEST, "GROUP4006", "그룹 매니저만 방장을 넘길 수 있습니다."),
    GROUP_CANNOT_KICK_MANAGER(HttpStatus.BAD_REQUEST, "GROUP4007", "매니저는 강퇴할 수 없습니다."),
    GROUP_INVITATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "GROUP4008", "초대를 찾을 수 없습니다."),
    GROUP_ALREADY_INVITED(HttpStatus.BAD_REQUEST, "GROUP4009", "이미 초대된 멤버입니다."),

    // 루틴
    ROUTINE_NOT_FOUND(HttpStatus.BAD_REQUEST, "ROUTINE4001", "루틴을 찾을 수 없습니다."),
    SCHEDULE_TYPE_NOT_ROUTINE(HttpStatus.BAD_REQUEST, "ROUTINE4002", "스케줄 타입이 루틴이 아닙니다."),
    REPEAT_WEEK_IS_EMPTY(HttpStatus.BAD_REQUEST, "ROUTINE4003", "반복 요일이 비어있습니다."),

    // 투두
    TODO_NOT_FOUND(HttpStatus.BAD_REQUEST, "TODO4001", "투두를 찾을 수 없습니다."),
    SCHEDULE_TYPE_NOT_TODO(HttpStatus.BAD_REQUEST, "TODO4001", "스케줄 타입이 투두가 아닙니다."),

    // 담당자
    ASSIGNEE_NOT_FOUND(HttpStatus.BAD_REQUEST, "ASSIGNEE4001", "담당자를 찾을 수 없습니다."),

    // s3 이미지 업로드
    FILE_IS_EMPTY(HttpStatus.BAD_REQUEST, "S3FILE4001", "파일이 비어있습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "S3FILE4002",  "파일 업로드에 실패했습니다"),
    FILE_DELETE_FAILED(HttpStatus.BAD_REQUEST, "S3FILE4002",  "파일 삭제에 실패했습니다"),
    FILE_NOT_FOUND(HttpStatus.BAD_REQUEST, "S3FILE4003",   "파일을 찾을 수 없습니다"),
    FILE_READ_FAILED(HttpStatus.BAD_REQUEST, "S3FILE4003",   "파일을 읽을 수 없습니다"),


    ;

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
