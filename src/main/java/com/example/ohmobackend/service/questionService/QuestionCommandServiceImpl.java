package com.example.ohmobackend.service.questionService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.MemberHandler;
import com.example.ohmobackend.apiPayload.exception.handler.QuestionHandler;
import com.example.ohmobackend.converter.QuestionConverter;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.Question;
import com.example.ohmobackend.repository.QuestionRepository;
import com.example.ohmobackend.web.dto.questionDto.QuestionRequestDto;
import com.example.ohmobackend.web.dto.questionDto.QuestionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionCommandServiceImpl implements QuestionCommandService {

    final QuestionRepository questionRepository;

    @Override
    public void addQuestion(Member member, QuestionRequestDto.QuestionRegisterDto request) {
        Question question = QuestionConverter.questionDtoToEntity(request, member);
        questionRepository.save(question);
    }

    @Override
    @Transactional
    public QuestionResponseDto.QuestionDto updateQuestion(Long questionId, QuestionRequestDto.QuestionUpdateDto request, Member member) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.QUESTION_NOT_FOUND));

        if (question.getMember() != member) {
            throw new MemberHandler(ErrorStatus.INVALID_MEMBER);
        }

        question.update(request.getQuestionContent(), request.getEmoji());
        return QuestionConverter.toQuestionResponseDto(question);
    }

    @Override
    @Transactional
    public void deleteQuestion(Long questionId, Member member) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.QUESTION_NOT_FOUND));

        if (question.getMember() != member) {
            throw new MemberHandler(ErrorStatus.INVALID_MEMBER);
        }

        questionRepository.delete(question);
    }
}
