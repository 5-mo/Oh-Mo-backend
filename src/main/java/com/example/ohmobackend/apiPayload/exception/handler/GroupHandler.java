package com.example.ohmobackend.apiPayload.exception.handler;

import com.example.ohmobackend.apiPayload.code.BaseErrorCode;
import com.example.ohmobackend.apiPayload.exception.GeneralException;

public class GroupHandler extends GeneralException {

    public GroupHandler(BaseErrorCode code) {
        super(code);
    }
}
