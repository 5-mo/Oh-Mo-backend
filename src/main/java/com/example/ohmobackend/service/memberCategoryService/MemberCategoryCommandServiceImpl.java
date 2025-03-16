package com.example.ohmobackend.service.memberCategoryService;

import com.example.ohmobackend.converter.MemberCategoryConverter;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberCategory;
import com.example.ohmobackend.repository.MemberCategoryRepository;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryDtoRequest;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberCategoryCommandServiceImpl implements MemberCategoryCommandService {

    final MemberCategoryRepository memberCategoryRepository;

    @Override
    public MemberCategoryResponseDto.addCategoryResponseDto addMemberCategory(MemberCategoryDtoRequest.addCategoryRequest request, Member member) {
        MemberCategory memberCategory = MemberCategoryConverter.toMemberCategoryEntity(request, member);
        memberCategoryRepository.save(memberCategory);
        return MemberCategoryConverter.toAddCategoryResponseDto(memberCategory);
    }
}
