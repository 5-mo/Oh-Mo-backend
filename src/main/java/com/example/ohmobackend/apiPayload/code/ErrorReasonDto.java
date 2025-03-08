package com.example.ohmobackend.apiPayload.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorReasonDto {
    private HttpStatus httpStatus;

    private final boolean isSuccess;
    private final String code;
    private final String message;
    private final String detail;

    public static class ErrorReasonDtoBuilder {
        public ErrorReasonDtoBuilder detail(String detail) {
            this.detail = detail;
            return this;
        }
    }

    public boolean getIsSuccess(){return isSuccess;}

}
