package com.example.ohmobackend.apiPayload.exception.handler;

import com.example.ohmobackend.apiPayload.code.BaseErrorCode;
import com.example.ohmobackend.apiPayload.exception.GeneralException;

public class S3Handler extends GeneralException {

    public S3Handler(BaseErrorCode code) {
        super(code);
    }
}
