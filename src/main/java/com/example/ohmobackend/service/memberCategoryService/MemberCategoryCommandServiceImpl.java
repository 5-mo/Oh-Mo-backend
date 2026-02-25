package com.example.ohmobackend.service.memberCategoryService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.MemberCategoryHandler;
import com.example.ohmobackend.apiPayload.exception.handler.MemberHandler;
import com.example.ohmobackend.converter.MemberCategoryConverter;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberCategory;
import com.example.ohmobackend.repository.MemberCategoryRepository;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryDtoRequest;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberCategoryCommandServiceImpl implements MemberCategoryCommandService {

    final MemberCategoryRepository memberCategoryRepository;

    @Override
    public MemberCategoryResponseDto.CategoryResponseDto addMemberCategory(MemberCategoryDtoRequest.addCategoryRequest request, Member member) {
        MemberCategory memberCategory = MemberCategoryConverter.toMemberCategoryEntity(request, member);
        memberCategoryRepository.save(memberCategory);
        return MemberCategoryConverter.toAddCategoryResponseDto(memberCategory);
    }

    @Override
    @Transactional
    public MemberCategoryResponseDto.CategoryResponseDto updateMemberCategory(Long categoryId, MemberCategoryDtoRequest.UpdateCategoryRequest request, Member member) {
        MemberCategory memberCategory = memberCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND));

        if (memberCategory.getMember() != member) {
            throw new MemberHandler(ErrorStatus.INVALID_MEMBER_CATEGORY);
        }

        memberCategory.update(request.getCategoryName(), request.getColor());
        return MemberCategoryConverter.toAddCategoryResponseDto(memberCategory);
    }

    @Override
    @Transactional
    public void deleteMemberCategory(Long categoryId, Member member) {
        MemberCategory memberCategory = memberCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND));

        if (memberCategory.getMember() != member) {
            throw new MemberHandler(ErrorStatus.INVALID_MEMBER_CATEGORY);
        }

        memberCategoryRepository.delete(memberCategory);
    }
}
