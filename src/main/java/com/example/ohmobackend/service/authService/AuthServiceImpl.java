package com.example.ohmobackend.service.authService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.AuthHandler;
import com.example.ohmobackend.apiPayload.exception.handler.MemberHandler;
import com.example.ohmobackend.converter.MemberConverter;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.repository.MemberRepository;
import com.example.ohmobackend.security.JwtToken;
import com.example.ohmobackend.security.principal.PrincipalDetailsService;
import com.example.ohmobackend.security.provider.TokenProvider;
import com.example.ohmobackend.service.memberService.MemberCommandServiceImpl;
import com.example.ohmobackend.web.dto.memberDto.MemberRequestDto;
import com.example.ohmobackend.web.dto.memberDto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final MemberRepository memberRepository;
    private final MemberCommandServiceImpl memberCommandService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PrincipalDetailsService principalDetailsService;


    public MemberResponseDto.MemberInfoResponseDto signup(MemberRequestDto.SignupRequestDto request, MultipartFile profileImage) {
        String email = request.getEmail();
        String password = request.getPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        if (memberRepository.existsByEmail(email)) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_EXISTS);
        }

        String profileImageUrl = memberCommandService.uploadProfileImageToS3(profileImage, email);
        Member member = MemberConverter.toEntity(request, encodedPassword, profileImageUrl);
        memberRepository.save(member);

        // default 카테고리 추가
        memberCommandService.saveDefaultCategory(member, ScheduleType.TO_DO);
        memberCommandService.saveDefaultCategory(member, ScheduleType.ROUTINE);

        return MemberConverter.toMemberInfoResponseDto(member);
    }

    public MemberResponseDto.LoginResponseDto login(MemberRequestDto.LoginRequestDto request) {
        String email = request.getEmail();
        String password = request.getPassword();
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 UserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = tokenProvider.generateTokenDto(authentication);

        Member member = memberCommandService.findMemberByEmail(email);
        member.updateRefreshToken(jwtToken.getRefreshToken());

        return MemberConverter.toLoginResponseDto(member, jwtToken);
    }

    public MemberResponseDto.LoginResponseDto reissue(String refreshToken) {
        tokenProvider.validateToken(refreshToken);
        String email = tokenProvider.getEmail(refreshToken);
        Member member = memberCommandService.findMemberByEmail(email);

        // DB에 저장된 Refresh Token과 비교
        if (member.getRefreshToken() == null ||
                !member.getRefreshToken().equals(refreshToken)) {
            throw new AuthHandler(ErrorStatus.INVALID_TOKEN);
        }

        UserDetails userDetails =
                principalDetailsService.loadUserByUsername(member.getEmail());

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        JwtToken jwtToken = tokenProvider.generateTokenDto(authentication);

        // Refresh Token 갱신
        member.updateRefreshToken(jwtToken.getRefreshToken());

        return MemberConverter.toLoginResponseDto(member, jwtToken);
    }

    public void logout(String accessToken) {
        String email = tokenProvider.getEmail(accessToken);
        Member member = memberCommandService.findMemberByEmail(email);
        // Refresh Token 제거
        member.updateRefreshToken(null);
    }

    public void withdraw(Member member) {
        memberCommandService.deleteMemberProfileImageFromS3(member);
        memberRepository.delete(member);
    }
}
