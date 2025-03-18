package com.example.ohmobackend.apiPayload.exception.handler;

import com.example.ohmobackend.apiPayload.code.BaseErrorCode;
import com.example.ohmobackend.apiPayload.exception.GeneralException;

public class MemberCategoryHandler extends GeneralException {

    public MemberCategoryHandler(BaseErrorCode code) {
        super(code);
    }
}
