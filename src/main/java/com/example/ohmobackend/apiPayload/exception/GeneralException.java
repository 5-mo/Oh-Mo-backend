package com.example.ohmobackend.apiPayload.exception;

import com.example.ohmobackend.apiPayload.code.BaseErrorCode;
import com.example.ohmobackend.apiPayload.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {
    private BaseErrorCode code;
    public ErrorReasonDto getErrorReason() {
        return this.code.getReason();
    }
    public ErrorReasonDto getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}
