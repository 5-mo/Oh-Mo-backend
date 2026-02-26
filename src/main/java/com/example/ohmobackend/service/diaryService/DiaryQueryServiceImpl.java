package com.example.ohmobackend.service.diaryService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.DiaryHandler;
import com.example.ohmobackend.converter.DiaryConverter;
import com.example.ohmobackend.domain.Diary;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.repository.DiaryRepository;
import com.example.ohmobackend.web.dto.diaryDto.DiaryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryQueryServiceImpl implements DiaryQueryService{

    final DiaryRepository diaryRepository;

    @Override
    public DiaryResponseDto.AddDiaryResponseDto getDiary(Member member, LocalDate date) {
        Diary diary = diaryRepository.findByMemberAndDate(member, date);

        if(diary == null) {
            throw new DiaryHandler(ErrorStatus.DIARY_NOT_FOUND);
        }

        return DiaryConverter.toDiaryResponseDto(diary);
    }
}
