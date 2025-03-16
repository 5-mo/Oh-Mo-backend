package com.example.ohmobackend.security.handler;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(hidden = true)
/* 인증 객체 정보를 가지고 다니는 커스텀 어노테이션, AuthDetail의 필드인 member를 가져온다. */
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : member")
public @interface AuthUser {
}