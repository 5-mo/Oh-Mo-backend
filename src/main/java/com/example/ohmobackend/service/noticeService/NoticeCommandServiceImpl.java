package com.example.ohmobackend.service.noticeService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.GroupHandler;
import com.example.ohmobackend.converter.NoticeConverter;
import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.Notice;
import com.example.ohmobackend.repository.GroupRepository;
import com.example.ohmobackend.repository.MemberGroupRepository;
import com.example.ohmobackend.repository.NoticeRepository;
import com.example.ohmobackend.web.dto.noticeDto.NoticeRequestDto;
import com.example.ohmobackend.web.dto.noticeDto.NoticeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeCommandServiceImpl implements NoticeCommandService {

    private final GroupRepository groupRepository;
    private final NoticeRepository noticeRepository;
    private final MemberGroupRepository memberGroupRepository;

    public NoticeResponseDto.NoticeDto addNotice(NoticeRequestDto.AddNoticeDto requestDto, Member member) {
        Group group = groupRepository.findById(requestDto.getGroupId())
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_NOT_FOUND));

        validateExistMember(member, group);

        Notice notice = NoticeConverter.toNoticeEntity(requestDto, group);
        noticeRepository.save(notice);

        NoticeResponseDto.NoticeDto noticeDto = NoticeConverter.toNoticeDto(notice);
        return noticeDto;
    }

    @Override
    @CacheEvict(value = "noticesByMonth", key = "#groupId + '_' + #yearMonth")
    public NoticeResponseDto.NoticeDto pathNotice(Long noticeId, NoticeRequestDto.PatchNoticeDto requestDto, Member member) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_NOT_FOUND));

        validateExistMember(member, notice.getGroup());

        if (requestDto.getNotice() != null) {
            notice.updateNotice(requestDto.getNotice());
        }

        if (requestDto.getDate() != null) {
            notice.updateDate(requestDto.getDate());
        }

        noticeRepository.save(notice);

        NoticeResponseDto.NoticeDto noticeDto = NoticeConverter.toNoticeDto(notice);
        return noticeDto;
    }

    @Override
    @CacheEvict(value = "noticesByMonth", key = "#groupId + '_' + #yearMonth")
    public void deleteNotice(Long noticeId, Member member) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_NOT_FOUND));

        validateExistMember(member, notice.getGroup());
        noticeRepository.deleteById(noticeId);
    }

    private void validateExistMember(Member member, Group group) {
        if (!memberGroupRepository.findByGroupAndMember(group, member).isEmpty()) {
            throw new GroupHandler(ErrorStatus.GROUP_EXISTS_MEMBER);
        }
    }
}
