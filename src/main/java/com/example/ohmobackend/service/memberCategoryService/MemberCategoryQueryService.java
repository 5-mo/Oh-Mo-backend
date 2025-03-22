package com.example.ohmobackend.service.memberCategoryService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryResponseDto;

import java.util.List;

public interface MemberCategoryQueryService {

    public List<MemberCategoryResponseDto.CategoryResponseDto> getMemberCategory(ScheduleType scheduleType, Member member);
}
