package com.example.ohmobackend.service.groupService;

import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberGroup;
import com.example.ohmobackend.repository.GroupRepository;
import com.example.ohmobackend.repository.MemberGroupRepository;
import com.example.ohmobackend.web.dto.groupDto.GroupRequestDto;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GroupCommandServiceImplTest {

    @InjectMocks
    private GroupCommandServiceImpl groupCommandService;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private MemberGroupRepository memberGroupRepository;

    private Member member;
    private GroupRequestDto.AddGroupRequestDto requestDto;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .nickname("테스트유저")
                .email("test@example.com")
                .build();

        requestDto = GroupRequestDto.AddGroupRequestDto.builder()
                .groupName("테스트 그룹")
                .groupColor("테스트 그룹 컬러")
                .numPeople(4)
                .nickname("매니저닉네임")
                .build();
    }

    @Test
    void addGroup_success_bdd() {
        // given (테스트 준비)
        Group groupToSave = Group.builder()
                .groupName(requestDto.getGroupName())
                .groupColor(requestDto.getGroupColor())
                .numPeople(requestDto.getNumPeople())
                .build();

        Group savedGroup = Group.builder()
                .groupName(groupToSave.getGroupName())
                .groupCode(groupToSave.getGroupCode())
                .groupColor(groupToSave.getGroupColor())
                .numPeople(groupToSave.getNumPeople())
                .build();

        MemberGroup savedMemberGroup = MemberGroup.builder()
                .member(member)
                .group(savedGroup)
                .nickname(requestDto.getNickname())
                .build();

        given(groupRepository.save(any(Group.class))).willReturn(savedGroup);
        given(memberGroupRepository.save(any(MemberGroup.class))).willReturn(savedMemberGroup);

        // when
        GroupResponseDto.GroupDto responseDto = groupCommandService.addGroup(member, requestDto);

        // then (검증)
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getGroupName()).isEqualTo("테스트 그룹");

        // BDDMockito verify로 실제 호출 검증
        then(groupRepository).should().save(any(Group.class));
        then(memberGroupRepository).should().save(any(MemberGroup.class));
    }
}
