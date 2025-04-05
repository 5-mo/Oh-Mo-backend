package com.example.ohmobackend.service.diaryService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.diaryDto.DiaryRequestDto;
import com.example.ohmobackend.web.dto.diaryDto.DiaryResponseDto;

public interface DiaryCommandService {

    public DiaryResponseDto.AddDiaryResponseDto addDiary(Member member, DiaryRequestDto.AddDiaryRequestDto requestDto);
}
