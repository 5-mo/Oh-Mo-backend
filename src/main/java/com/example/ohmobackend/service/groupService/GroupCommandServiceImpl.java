package com.example.ohmobackend.service.groupService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.GroupHandler;
import com.example.ohmobackend.converter.GroupConverter;
import com.example.ohmobackend.converter.MemberGroupConverter;
import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberGroup;
import com.example.ohmobackend.domain.enums.GroupRole;
import com.example.ohmobackend.repository.GroupRepository;
import com.example.ohmobackend.repository.MemberGroupRepository;
import com.example.ohmobackend.web.dto.groupDto.GroupRequestDto;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import com.example.ohmobackend.web.dto.memberGroupDto.MemberGroupResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupCommandServiceImpl implements GroupCommandService {

    final GroupRepository groupRepository;
    final MemberGroupRepository memberGroupRepository;
    final GroupValidator groupValidator;

    @Override
    public GroupResponseDto.GroupDto addGroup(Member member, GroupRequestDto.AddGroupRequestDto requestDto) {
        Group group = GroupConverter.toGroupEntity(requestDto);
        Group newGroup = groupRepository.save(group);

        MemberGroup memberGroup = MemberGroupConverter.toMemberGroupEntity(member, group, GroupRole.MANAGER);
        memberGroupRepository.save(memberGroup);
        return GroupConverter.toGroupWithManagerDto(newGroup, member);
    }

    @Override
    public GroupResponseDto.GroupDto enterGroup(Member member, GroupRequestDto.EnterGroupRequestDto requestDto) {
        Group group = groupRepository.findByGroupCode(requestDto.getGroupCode())
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_NOT_FOUND));

        groupValidator.validateGroupPassword(requestDto.getGroupPassword(), group);
        groupValidator.validateExistMember(member, group);
        groupValidator.validateGroupCount(group);

        MemberGroup memberGroup = MemberGroupConverter.toMemberGroupEntity(member, group, GroupRole.MEMBER);
        memberGroupRepository.save(memberGroup);
        return GroupConverter.toGroupDto(group);
    }

    @Override
    @Transactional
    public MemberGroupResponseDto.MemberGroupInfoDto updateNickname(Member member, GroupRequestDto.AddGroupNicknameDto requestDto) {
        Group group = groupRepository.findById(requestDto.getGroupId())
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_NOT_FOUND));
        groupValidator.validateDuplicateNickname(group, requestDto.getNickname());

        MemberGroup memberGroup = memberGroupRepository.findByMemberAndGroup(member, group);
        memberGroup.updateNickname(requestDto.getNickname());
        return MemberGroupConverter.toMemberGroupInfoDto(memberGroup);
    }

    @Override
    @Transactional
    public void leaveGroup(Member member, GroupRequestDto.LeaveGroupRequestDto requestDto) {
        Group group = groupRepository.findById(requestDto.getGroupId())
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_NOT_FOUND));

        MemberGroup memberGroup = groupValidator.validateMemberGroup(member, group);

        if (memberGroup.getRole() == GroupRole.MANAGER) {
            long memberCount = memberGroupRepository.countByGroup(group);
            if (memberCount > 1) {
                throw new GroupHandler(ErrorStatus.GROUP_MANAGER_CANNOT_LEAVE);
            }
            groupRepository.delete(group);
        } else {
            memberGroupRepository.delete(memberGroup);
        }
    }
}
