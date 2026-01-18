package com.example.ohmobackend.web.dto.memberGroupDto;

import com.example.ohmobackend.domain.enums.GroupRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberGroupResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberGroupInfoDto{
        private Long memberGroupId;
        private GroupRole role;
        private String nickname;
    }
}
