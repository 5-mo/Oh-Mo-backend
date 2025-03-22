package com.example.ohmobackend.service.memberCategoryService;

import com.example.ohmobackend.converter.MemberCategoryConverter;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberCategory;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.repository.MemberCategoryRepository;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberCategoryQueryServiceImpl implements MemberCategoryQueryService{

    final MemberCategoryRepository memberCategoryRepository;

    @Override
    public List<MemberCategoryResponseDto.CategoryResponseDto> getMemberCategory(ScheduleType scheduleType, Member member) {
        List<MemberCategory> categoryList = memberCategoryRepository.findByMemberAndScheduleType(member, scheduleType);

        return categoryList.stream()
                .map(memberCategory -> MemberCategoryConverter.toAddCategoryResponseDto(memberCategory))
                .collect(Collectors.toList());
    }
}
