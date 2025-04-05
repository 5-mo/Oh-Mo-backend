package com.example.ohmobackend.service.diaryService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.diaryDto.DiaryResponseDto;

import java.time.LocalDate;

public interface DiaryQueryService {

    public DiaryResponseDto.AddDiaryResponseDto getDiary(Member member, LocalDate date);
}
