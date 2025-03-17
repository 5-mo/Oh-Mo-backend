package com.example.ohmobackend.apiPayload.exception.handler;

import com.example.ohmobackend.apiPayload.code.BaseErrorCode;
import com.example.ohmobackend.apiPayload.exception.GeneralException;

public class ScheduleHandler extends GeneralException {

    public ScheduleHandler(BaseErrorCode code) {
        super(code);
    }
}
