package com.example.ohmobackend.service.groupService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.GroupHandler;
import com.example.ohmobackend.converter.GroupConverter;
import com.example.ohmobackend.converter.MemberGroupConverter;
import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberGroup;
import com.example.ohmobackend.repository.GroupRepository;
import com.example.ohmobackend.repository.MemberGroupRepository;
import com.example.ohmobackend.web.dto.groupDto.GroupRequestDto;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupCommandServiceImpl implements GroupCommandService {

    final GroupRepository groupRepository;
    final MemberGroupRepository memberGroupRepository;

    @Override
    public GroupResponseDto.GroupDto addGroup(Member member, GroupRequestDto.AddGroupRequestDto requestDto) {
        Group group = GroupConverter.toGroupEntity(requestDto);
        Group newGroup = groupRepository.save(group);

        MemberGroup memberGroup = MemberGroupConverter.managerToMemberGroupEntity(member, group, requestDto.getNickname());
        memberGroupRepository.save(memberGroup);
        return GroupConverter.toGroupDto(newGroup);
    }

    @Override
    public GroupResponseDto.GroupDto enterGroup(Member member, GroupRequestDto.EnterGroupRequestDto requestDto) {
        Group group = groupRepository.findByGroupCode(requestDto.getGroupCode())
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_NOT_FOUND));

        validateGroupPassword(requestDto.getGroupPassword(), group);
        validateDuplicateNickname(group, requestDto.getNickname());
        validateGroupCount(group);

        MemberGroup memberGroup = MemberGroupConverter.managerToMemberGroupEntity(member, group, requestDto.getNickname());
        memberGroupRepository.save(memberGroup);
        return GroupConverter.toGroupDto(group);
    }

    private static void validateGroupPassword(String password, Group group) {
        if (password.equals(group.getGroupPassword())) {
            throw new GroupHandler(ErrorStatus.GROUP_INVALID_PASSWORD);
        }
    }

    private void validateDuplicateNickname(Group group, String nickname) {
        boolean nicknameExists = memberGroupRepository
                .existsByGroupAndNickname(group, nickname);

        if (nicknameExists) {
            throw new GroupHandler(ErrorStatus.GROUP_NICKNAME_DUPLICATED);
        }
    }

    private void validateGroupCount(Group group) {
        long currentCount = memberGroupRepository.countByGroup(group);

        if (currentCount >= group.getNumPeople()) {
            throw new GroupHandler(ErrorStatus.GROUP_MEMBER_LIMIT_EXCEEDED);
        }
    }
}
