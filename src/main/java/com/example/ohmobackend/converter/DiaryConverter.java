package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Diary;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.diaryDto.DiaryRequestDto;
import com.example.ohmobackend.web.dto.diaryDto.DiaryResponseDto;

public class DiaryConverter {

    public static Diary toEntity(Member member, DiaryRequestDto.AddDiaryRequestDto requestDto) {
        return Diary.builder()
                .content(requestDto.getContent())
                .date(requestDto.getDate())
                .member(member)
                .build();
    }

    public static DiaryResponseDto.AddDiaryResponseDto toDiaryResponseDto(Diary diary) {
        return DiaryResponseDto.AddDiaryResponseDto.builder()
                .id(diary.getId())
                .date(diary.getDate())
                .content(diary.getContent())
                .build();
    }
}
