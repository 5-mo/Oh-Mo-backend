package com.example.ohmobackend.service.questionService;

import com.example.ohmobackend.converter.QuestionConverter;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.Question;
import com.example.ohmobackend.repository.QuestionRepository;
import com.example.ohmobackend.web.dto.questionDto.QuestionRequestDto;
import com.example.ohmobackend.web.dto.questionDto.QuestionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionCommandServiceImpl implements QuestionCommandService{

    final QuestionRepository questionRepository;

    @Override
    public QuestionResponseDto.QuestionDto addQuestion(Member member, QuestionRequestDto.QuestionRegisterDto request) {
        Question question = QuestionConverter.questionDtoToEntity(request, member);
        questionRepository.save(question);
        return QuestionConverter.toQuestionResponseDto(question, null);
    }
}
