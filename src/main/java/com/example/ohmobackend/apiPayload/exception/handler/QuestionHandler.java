package com.example.ohmobackend.apiPayload.exception.handler;

import com.example.ohmobackend.apiPayload.code.BaseErrorCode;
import com.example.ohmobackend.apiPayload.exception.GeneralException;

public class QuestionHandler extends GeneralException {

    public QuestionHandler(BaseErrorCode code) {
        super(code);
    }
}