package com.example.ohmobackend.service.memberService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.AuthHandler;
import com.example.ohmobackend.apiPayload.exception.handler.MemberHandler;
import com.example.ohmobackend.converter.MemberCategoryConverter;
import com.example.ohmobackend.converter.MemberConverter;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberCategory;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.repository.MemberCategoryRepository;
import com.example.ohmobackend.repository.MemberRepository;
import com.example.ohmobackend.security.JwtToken;
import com.example.ohmobackend.security.principal.PrincipalDetails;
import com.example.ohmobackend.security.principal.PrincipalDetailsService;
import com.example.ohmobackend.security.provider.TokenProvider;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryDtoRequest;
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

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberCategoryRepository memberCategoryRepository;
    private final PrincipalDetailsService principalDetailsService;

    @Override
    public MemberResponseDto.SignupResponseDto signup(MemberRequestDto.SignupRequestDto request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_EXISTS);
        }

        Member member = MemberConverter.toEntity(request, bCryptPasswordEncoder.encode(request.getPassword()));
        Member newMember = memberRepository.save(member);

        // default 카테고리 추가
        saveDefaultCategory(member, ScheduleType.TO_DO);
        saveDefaultCategory(member, ScheduleType.ROUTINE);

        return MemberConverter.toDto(newMember);
    }

    private void saveDefaultCategory(Member member, ScheduleType scheduleType) {
        MemberCategoryDtoRequest.addCategoryRequest todoCategory = MemberCategoryDtoRequest.addCategoryRequest.builder()
                .color("#000000")
                .scheduleType(scheduleType)
                .categoryName("default")
                .build();
        MemberCategory defaultCategoryEntity = MemberCategoryConverter.toMemberCategoryEntity(todoCategory, member);
        memberCategoryRepository.save(defaultCategoryEntity);
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
                        .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        member.updateRefreshToken(jwtToken.getRefreshToken());

        return MemberConverter.toLoginResponseDto(member, jwtToken);
    }

    @Override
    public Member getByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Transactional
    public MemberResponseDto.LoginResponseDto reissue(String refreshToken) {
        tokenProvider.validateToken(refreshToken);
        String email = tokenProvider.getEmail(refreshToken);
        System.out.println("Reissue email from token: " + email);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 4️⃣ DB에 저장된 Refresh Token과 비교
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

    @Transactional
    @Override
    public void logout(String accessToken) {
        String email = tokenProvider.getEmail(accessToken);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // Refresh Token 제거
        member.updateRefreshToken(null);
    }

}
