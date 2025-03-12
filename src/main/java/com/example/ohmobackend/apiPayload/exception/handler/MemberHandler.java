package com.example.ohmobackend.apiPayload.exception.handler;

import com.example.ohmobackend.apiPayload.code.BaseErrorCode;
import com.example.ohmobackend.apiPayload.exception.GeneralException;

public class MemberHandler extends GeneralException {

    public MemberHandler(BaseErrorCode code) {
        super(code);
    }
}
