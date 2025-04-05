package com.example.ohmobackend.service.diaryService;

import com.example.ohmobackend.converter.DiaryConverter;
import com.example.ohmobackend.domain.Diary;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.repository.DiaryRepository;
import com.example.ohmobackend.web.dto.diaryDto.DiaryRequestDto;
import com.example.ohmobackend.web.dto.diaryDto.DiaryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryCommandServiceImpl implements DiaryCommandService{

    final DiaryRepository diaryRepository;

    @Override
    @Transactional
    public DiaryResponseDto.AddDiaryResponseDto addDiary(Member member, DiaryRequestDto.AddDiaryRequestDto requestDto) {
        Diary diary = diaryRepository.findByMemberAndDate(member, requestDto.getDate());

        if(diary == null) {
            diary = DiaryConverter.toEntity(member, requestDto);
        } else {
            diary.updateContent(requestDto.getContent());
        }

        diaryRepository.save(diary);

        return DiaryConverter.toDiaryResponseDto(diary);
    }
}
