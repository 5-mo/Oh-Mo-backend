package com.example.ohmobackend.service.groupService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.GroupHandler;
import com.example.ohmobackend.converter.GroupConverter;
import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.GroupInvitation;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberGroup;
import com.example.ohmobackend.repository.GroupInvitationRepository;
import com.example.ohmobackend.repository.GroupRepository;
import com.example.ohmobackend.repository.MemberGroupRepository;
import com.example.ohmobackend.web.dto.groupDto.GroupRequestDto;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupQueryServiceImpl implements GroupQueryService {

    private final GroupRepository groupRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final GroupInvitationRepository groupInvitationRepository;
    private final GroupValidator groupValidator;

    public GroupResponseDto.GroupMembersDto getGroupMembers(Long groupId, Member member) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_NOT_FOUND));

        // 그룹의 멤버가 아닌 경우
        groupValidator.validateMemberGroup(member, group);

        List<MemberGroup> memberGroup = memberGroupRepository.findAllByGroup(group);
        return GroupConverter.toGroupMembersDto(group, memberGroup);
    }

    public List<GroupResponseDto.GroupDto> getGroups(Member member) {
        List<MemberGroup> managerMemberGroups = memberGroupRepository.findHostMemberGroupsByMember(member);
        List<GroupResponseDto.GroupDto> groups = managerMemberGroups.stream().map(
                memberGroup -> GroupConverter.toGroupWithManagerDto(memberGroup.getGroup(), memberGroup.getMember())
        ).collect(Collectors.toList());

        return groups;
    }

    @Override
    @Transactional
    public void deleteGroup(Member member, GroupRequestDto.DeleteGroupRequestDto request) {
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_NOT_FOUND));

        groupValidator.validateMemberGroup(member, group);
        groupRepository.delete(group);
    }

    @Override
    public List<GroupResponseDto.InvitationDto> getInvitations(Member member) {
        List<GroupInvitation> invitations = groupInvitationRepository.findAllByInvitedMember(member);
        return invitations.stream()
                .map(invitation -> GroupResponseDto.InvitationDto.builder()
                        .invitationId(invitation.getId())
                        .groupId(invitation.getGroup().getId())
                        .groupName(invitation.getGroup().getGroupName())
                        .groupColor(invitation.getGroup().getGroupColor())
                        .invitedByNickname(invitation.getInvitedBy().getNickname())
                        .build())
                .collect(Collectors.toList());
    }
}
