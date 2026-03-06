package com.example.ohmobackend.service;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberGroup;
import com.example.ohmobackend.repository.EmitterRepository;
import com.example.ohmobackend.repository.MemberGroupRepository;
import com.example.ohmobackend.service.scheduleService.GroupScheduleQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class SseService {
    private final EmitterRepository emitterRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final GroupScheduleQueryService groupScheduleQueryService;

    public void subscribe(Long groupId, SseEmitter emitter, Member member) {
        // 그룹 멤버인지 검증
        Group group = groupScheduleQueryService.getGroup(groupId);
        validateMemberGroup(member, group);

        emitterRepository.save(groupId, emitter);

        // 연결 종료/타임아웃/에러 시 정리 로직
        emitter.onCompletion(() -> emitterRepository.delete(groupId, emitter));
        emitter.onTimeout(() -> emitterRepository.delete(groupId, emitter));
        emitter.onError(e -> emitterRepository.delete(groupId, emitter));

        // 503 에러 방지를 위한 첫 더미 데이터 전송
        try {
            emitter.send(SseEmitter.event()
                    .name("connect") // 이벤트 이름
                    .data("EventStream Connected [groupId=" + groupId + "]"));
        } catch (IOException e) {
            log.error("SSE 연결 첫 메시지 전송 실패", e);
        }
    }

    private MemberGroup validateMemberGroup(Member member, Group group) {
        return memberGroupRepository.findByGroupAndMember(group, member)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND));
    }
}