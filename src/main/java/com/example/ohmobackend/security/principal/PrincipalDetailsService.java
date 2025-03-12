package com.example.ohmobackend.security.principal;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.MemberHandler;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = userRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return new PrincipalDetails(member);
    }
}
