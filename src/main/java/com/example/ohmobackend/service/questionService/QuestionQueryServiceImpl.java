package com.example.ohmobackend.service.questionService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.QuestionHandler;
import com.example.ohmobackend.converter.QuestionConverter;
import com.example.ohmobackend.domain.Answer;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.Question;
import com.example.ohmobackend.repository.AnswerRepository;
import com.example.ohmobackend.repository.QuestionRepository;
import com.example.ohmobackend.web.dto.questionDto.QuestionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionQueryServiceImpl implements QuestionQueryService {

    final QuestionRepository questionRepository;
    final AnswerRepository answerRepository;

    @Override
    public List<QuestionResponseDto.QuestionDto> getQuestions(Member member) {
        List<Question> questions = questionRepository.findAllByMember(member);

        if (questions.isEmpty()) {
            throw new QuestionHandler(ErrorStatus.QUESTION_NOT_FOUND);
        }

        return questions.stream()
                .map(QuestionConverter::toQuestionResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionResponseDto.QuestionWithAnswerResponseDto> getQuestionsWithAnswers(Member member, LocalDate date) {
        List<Question> questions = questionRepository.findAllByMember(member);

        if (questions.isEmpty()) {
            throw new QuestionHandler(ErrorStatus.QUESTION_NOT_FOUND);
        }

        return questions.stream()
                .map(question -> {
                    List<Answer> answers = answerRepository.findAllByQuestionAndDate(question, date);
                    return QuestionConverter.toQuestionWithAnswerResponseDto(question, answers);
                })
                .collect(Collectors.toList());
    }
}
