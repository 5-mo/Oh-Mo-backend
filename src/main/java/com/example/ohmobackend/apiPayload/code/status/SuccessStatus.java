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
    SCHEDULE_OK(HttpStatus.OK, "SCHEDULE2003", "스케줄 조회가 완료되었습니다."),
    SCHEDULE_UPDATE_DATE_OK(HttpStatus.OK, "SCHEDULE2004", "스케줄 날짜 변경이 완료되었습니다."),
    SCHEDULE_UPDATE_ALARM_TIME_OK(HttpStatus.OK, "SCHEDULE2004", "스케줄 알람 시간 변경이 완료되었습니다."),
    SCHEDULE_ROUTINE_STATUS_OK(HttpStatus.OK, "SCHEDULE2005", "루틴 완료 상태 조회가 완료되었습니다."),
    SCHEDULE_COMPLETION_RATE_OK(HttpStatus.OK, "SCHEDULE2006", "completion rate 조회가 완료되었습니다."),
    SCHEDULE_ASSIGNEE_OK(HttpStatus.OK, "SCHEDULE2007", "일정 담당자 등록이 완료되었습니다."),

    // 질문 관련 응답
    QUESTION_REGISTER_OK(HttpStatus.OK, "QUESTION2000", "질문 등록이 완료되었습니다."),
    QUESTION_OK(HttpStatus.OK, "QUESTION2001", "질문 조회가 완료되었습니다."),

    // 답 관련 응답
    ANSWER_REGISTER_OK(HttpStatus.OK, "ANSWER2000", "답 등록이 완료되었습니다."),

    // 데이로그 관련 응답
    EMOJI_REGISTER_OK(HttpStatus.OK, "DAY_LOG2000", "이모지 등록이 완료되었습니다."),

    // 일기 관련 응답
    DIARY_REGISTER_OK(HttpStatus.OK, "DIARY_2000", "일기 등록이 완료되었습니다."),
    DIARY_OK(HttpStatus.OK, "DIARY_2001", "일기 조회가 완료되었습니다."),

    // 그룹 관련
    GROUP_REGISTER_OK(HttpStatus.OK, "GROUP_2000", "그룹 등록이 완료되었습니다."),
    GROUP_FIND_OK(HttpStatus.OK, "GROUP_2001", "그룹 조회가 완료되었습니다."),
    GROUP_ENTER_OK(HttpStatus.OK, "GROUP_2002", "그룹 입장이 완료되었습니다."),
    GROUP_MEMBER_OK(HttpStatus.OK, "GROUP_2003", "그룹 멤버 조회가 완료되었습니다."),

    // 공지사항 관련
    GROUP_NOTICE_REGISTER_OK(HttpStatus.OK, "GROUP_2001", "그룹 공지사항 등록이 완료되었습니다."),
    GROUP_NOTICE_MODIFY_OK(HttpStatus.OK, "GROUP_2002", "그룹 공지사항 수정이 완료되었습니다."),
    GROUP_NOTICE_DELETE_OK(HttpStatus.OK, "GROUP_2003", "그룹 공지사항 삭제가 완료되었습니다."),

    // 루틴 관련
    ROUTINE_UPDATE_STATUS_OK(HttpStatus.OK, "ROUTINE_2000", "루틴 상태 변경이 완료되었습니다."),

    // 투두 관련
    TODO_UPDATE_STATUS_OK(HttpStatus.OK, "TODO_2000", "투두 상태 변경이 완료되었습니다."),

    ;


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