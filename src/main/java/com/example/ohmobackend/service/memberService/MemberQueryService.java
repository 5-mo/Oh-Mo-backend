package com.example.ohmobackend.service.memberService;

import com.example.ohmobackend.domain.Member;

public interface MemberQueryService {

    Member findMemberByEmail(String email);
}
