package com.example.ohmobackend.apiPayload.exception.handler;

import com.example.ohmobackend.apiPayload.code.BaseErrorCode;
import com.example.ohmobackend.apiPayload.exception.GeneralException;

public class DiaryHandler extends GeneralException {

    public DiaryHandler(BaseErrorCode code) {
        super(code);
    }
}
