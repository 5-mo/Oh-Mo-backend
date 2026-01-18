package com.example.ohmobackend.service.groupService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.GroupHandler;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberGroup;
import com.example.ohmobackend.repository.MemberGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupValidator {
    final MemberGroupRepository memberGroupRepository;

    public MemberGroup validateMemberGroup(Member member, Group group) {
        return memberGroupRepository.findByGroupAndMember(group, member)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND));
    }

    public void validateGroupPassword(String password, Group group) {
        if (!password.equals(group.getGroupPassword())) {
            throw new GroupHandler(ErrorStatus.GROUP_INVALID_PASSWORD);
        }
    }

    public void validateExistMember(Member member, Group group) {
        if (!memberGroupRepository.findByGroupAndMember(group, member).isEmpty()) {
            throw new GroupHandler(ErrorStatus.GROUP_EXISTS_MEMBER);
        }
    }

    public void validateDuplicateNickname(Group group, String nickname) {
        boolean nicknameExists = memberGroupRepository
                .existsByGroupAndNickname(group, nickname);

        if (nicknameExists) {
            throw new GroupHandler(ErrorStatus.GROUP_NICKNAME_DUPLICATED);
        }
    }

    public void validateGroupCount(Group group) {
        long currentCount = memberGroupRepository.countByGroup(group);

        if (currentCount >= group.getNumPeople()) {
            throw new GroupHandler(ErrorStatus.GROUP_MEMBER_LIMIT_EXCEEDED);
        }
    }
}
