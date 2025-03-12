package com.example.ohmobackend.service.memberService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.AuthHandler;
import com.example.ohmobackend.converter.MemberConverter;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.repository.MemberRepository;
import com.example.ohmobackend.security.JwtToken;
import com.example.ohmobackend.security.provider.TokenProvider;
import com.example.ohmobackend.web.dto.memberDto.MemberRequestDto;
import com.example.ohmobackend.web.dto.memberDto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    @Override
    public MemberResponseDto.SignupResponseDto signup(MemberRequestDto.SignupRequestDto request) {
        Member member = MemberConverter.toEntity(request, bCryptPasswordEncoder.encode(request.getPassword()));

        Member newMember = memberRepository.save(member);

        return MemberConverter.toDto(newMember);
    }

    @Transactional
    public MemberResponseDto.LoginResponseDto login(MemberRequestDto.LoginRequestDto loginRequest) {
        System.out.println(loginRequest.getPassword());
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 UserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = tokenProvider.generateTokenDto(authentication);

        Member member = memberRepository.findByEmail(authentication.getName())
                        .orElseThrow(() -> new AuthHandler(ErrorStatus.MEMBER_NOT_FOUND));
        member.updateRefreshToken(jwtToken.getRefreshToken());

        return MemberConverter.toLoginResponseDto(member, jwtToken);
    }

//    @Transactional
//    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
//        // 1. Refresh Token 검증
//        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
//            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
//        }
//
//        // 2. Access Token 에서 Member ID 가져오기
//        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
//
//        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
//        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
//                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
//
//        // 4. Refresh Token 일치하는지 검사
//        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
//            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
//        }
//
//        // 5. 새로운 토큰 생성
//        TokenDto tokenDto = null;
//        if (tokenProvider.refreshTokenPeriodCheck(refreshToken.getValue())) {
//            // 5-1. Refresh Token의 유효기간이 3일 미만일 경우 전체(Access / Refresh) 재발급
//            tokenDto = tokenProvider.generateTokenDto(authentication);
//
//            // 6. Refresh Token 저장소 정보 업데이트
//            RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
//            refreshTokenRepository.save(newRefreshToken);
//        } else {
//            // 5-2. Refresh Token의 유효기간이 3일 이상일 경우 Access Token만 재발급
//            tokenDto = tokenProvider.createAccessToken(authentication);
//        }
//
//        // 토큰 발급
//        return tokenDto;
//    }
}
