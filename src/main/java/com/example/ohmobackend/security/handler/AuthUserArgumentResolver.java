package com.example.ohmobackend.security.handler;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.AuthHandler;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.provider.TokenProvider;
import com.example.ohmobackend.service.memberService.MemberQueryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;
    private final MemberQueryService memberService;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 메서드 파라미터의 타입이 Member인지 확인
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws AuthHandler {
        // HTTP 요청에서 Authorization 헤더 가져오기
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = extractTokenFromHeader(request);

        // 토큰이 없거나 유효하지 않으면 예외 발생
        if (!StringUtils.hasText(token) || !tokenProvider.validateToken(token)) {
            throw new AuthHandler(ErrorStatus.INVALID_TOKEN);
        }

        // 토큰에서 이메일 추출 후 해당 Member 반환
        String email = tokenProvider.getEmail(token);
        return memberService.findMemberByEmail(email);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // "Bearer " 제거 후 토큰 반환
        }

        return null;
    }
}