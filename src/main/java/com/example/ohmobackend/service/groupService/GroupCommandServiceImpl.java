package com.example.ohmobackend.service.groupService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.GroupHandler;
import com.example.ohmobackend.apiPayload.exception.handler.MemberHandler;
import com.example.ohmobackend.converter.GroupConverter;
import com.example.ohmobackend.converter.MemberGroupConverter;
import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.GroupInvitation;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberGroup;
import com.example.ohmobackend.domain.enums.GroupRole;
import com.example.ohmobackend.repository.GroupInvitationRepository;
import com.example.ohmobackend.repository.GroupRepository;
import com.example.ohmobackend.repository.MemberGroupRepository;
import com.example.ohmobackend.repository.MemberRepository;
import com.example.ohmobackend.service.FcmService;
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
    final MemberRepository memberRepository;
    final GroupInvitationRepository groupInvitationRepository;
    final GroupValidator groupValidator;
    final FcmService fcmService;

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

    @Override
    @Transactional
    public void kickMember(Member member, GroupRequestDto.KickMemberRequestDto requestDto) {
        Group group = groupRepository.findById(requestDto.getGroupId())
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_NOT_FOUND));

        MemberGroup requester = groupValidator.validateMemberGroup(member, group);
        if (requester.getRole() != GroupRole.MANAGER) {
            throw new GroupHandler(ErrorStatus.GROUP_NOT_MANAGER);
        }

        MemberGroup targetMemberGroup = memberGroupRepository.findById(requestDto.getTargetMemberGroupId())
                .orElseThrow(() -> new GroupHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND));

        if (targetMemberGroup.getRole() == GroupRole.MANAGER) {
            throw new GroupHandler(ErrorStatus.GROUP_CANNOT_KICK_MANAGER);
        }

        memberGroupRepository.delete(targetMemberGroup);
    }

    @Override
    @Transactional
    public void transferManager(Member member, GroupRequestDto.TransferManagerRequestDto requestDto) {
        Group group = groupRepository.findById(requestDto.getGroupId())
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_NOT_FOUND));

        MemberGroup currentManager = groupValidator.validateMemberGroup(member, group);
        if (currentManager.getRole() != GroupRole.MANAGER) {
            throw new GroupHandler(ErrorStatus.GROUP_NOT_MANAGER);
        }

        MemberGroup targetMemberGroup = memberGroupRepository.findById(requestDto.getTargetMemberGroupId())
                .orElseThrow(() -> new GroupHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND));

        currentManager.updateRole(GroupRole.MEMBER);
        targetMemberGroup.updateRole(GroupRole.MANAGER);
    }

    @Override
    @Transactional
    public void inviteMember(Member member, GroupRequestDto.InviteMemberRequestDto requestDto) {
        Group group = groupRepository.findByIdWithLock(requestDto.getGroupId())
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_NOT_FOUND));

        MemberGroup requester = groupValidator.validateMemberGroup(member, group);
        if (requester.getRole() != GroupRole.MANAGER) {
            throw new GroupHandler(ErrorStatus.GROUP_NOT_MANAGER);
        }

        Member targetMember = memberRepository.findByEmail(requestDto.getTargetEmail())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        groupValidator.validateExistMember(targetMember, group);
        groupValidator.validateGroupCount(group);

        if (groupInvitationRepository.existsByGroupAndInvitedMember(group, targetMember)) {
            throw new GroupHandler(ErrorStatus.GROUP_ALREADY_INVITED);
        }

        GroupInvitation invitation = GroupInvitation.builder()
                .group(group)
                .invitedMember(targetMember)
                .invitedBy(member)
                .build();
        GroupInvitation savedInvitation = groupInvitationRepository.save(invitation);

        fcmService.sendInvitationNotification(targetMember.getFcmToken(), group.getGroupName(), savedInvitation.getId());
    }

    @Override
    @Transactional
    public void acceptInvitation(Member member, GroupRequestDto.InvitationActionRequestDto requestDto) {
        GroupInvitation invitation = groupInvitationRepository.findById(requestDto.getInvitationId())
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_INVITATION_NOT_FOUND));

        if (!invitation.getInvitedMember().getId().equals(member.getId())) {
            throw new GroupHandler(ErrorStatus.GROUP_INVITATION_NOT_FOUND);
        }

        Group group = invitation.getGroup();
        groupValidator.validateExistMember(member, group);
        groupValidator.validateGroupCount(group);

        MemberGroup newMemberGroup = MemberGroupConverter.toMemberGroupEntity(member, group, GroupRole.MEMBER);
        memberGroupRepository.save(newMemberGroup);
        groupInvitationRepository.delete(invitation);
    }

    @Override
    @Transactional
    public void rejectInvitation(Member member, GroupRequestDto.InvitationActionRequestDto requestDto) {
        GroupInvitation invitation = groupInvitationRepository.findById(requestDto.getInvitationId())
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_INVITATION_NOT_FOUND));

        if (!invitation.getInvitedMember().getId().equals(member.getId())) {
            throw new GroupHandler(ErrorStatus.GROUP_INVITATION_NOT_FOUND);
        }

        groupInvitationRepository.delete(invitation);
    }
}
