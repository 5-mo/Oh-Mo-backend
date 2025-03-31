package com.example.ohmobackend.service.answerService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.QuestionHandler;
import com.example.ohmobackend.converter.AnswerConverter;
import com.example.ohmobackend.domain.Answer;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.Question;
import com.example.ohmobackend.repository.AnswerRepository;
import com.example.ohmobackend.repository.QuestionRepository;
import com.example.ohmobackend.web.dto.answerDto.AnswerRequestDto;
import com.example.ohmobackend.web.dto.answerDto.AnswerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerCommandServiceImpl implements AnswerCommandService{

    final AnswerRepository answerRepository;
    final QuestionRepository questionRepository;

    @Override
    public AnswerResponseDto.AnswerDto addAnswer(Member member, AnswerRequestDto.AddAnswerDto requestDto) {
        Question question = questionRepository.findById(requestDto.getQuestionId())
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.QUESTION_NOT_FOUND));

        Answer answer = AnswerConverter.toEntity(requestDto, question);

        answerRepository.save(answer);

        return AnswerConverter.toAnswerResponseDto(answer);
    }
}
