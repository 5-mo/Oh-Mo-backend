package com.example.ohmobackend.service.memberCategoryService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryDtoRequest;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryResponseDto;

public interface MemberCategoryCommandService {

    public MemberCategoryResponseDto.CategoryResponseDto addMemberCategory(MemberCategoryDtoRequest.addCategoryRequest request, Member member);
}
