package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberCategory;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryDtoRequest;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryResponseDto;

public class MemberCategoryConverter {

    public static MemberCategory toMemberCategoryEntity(MemberCategoryDtoRequest.addCategoryRequest request, Member member) {
        return MemberCategory.builder()
                .categoryName(request.getCategoryName())
                .color(request.getColor())
                .scheduleType(request.getScheduleType())
                .member(member)
                .build();
    }

    public static MemberCategoryResponseDto.CategoryResponseDto toAddCategoryResponseDto(MemberCategory memberCategory) {
        return MemberCategoryResponseDto.CategoryResponseDto.builder()
                .id(memberCategory.getId())
                .categoryName(memberCategory.getCategoryName())
                .color(memberCategory.getColor())
                .scheduleType(memberCategory.getScheduleType())
                .build();
    }
}
