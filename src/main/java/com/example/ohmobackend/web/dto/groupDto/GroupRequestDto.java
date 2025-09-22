package com.example.ohmobackend.web.dto.groupDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class GroupRequestDto {

    @Setter
    @Getter
    @Builder
    public static class AddGroupRequestDto {
        private String groupName;
        private String groupCode;
        private String groupColor;
        private int numPeople;
        private String nickname;
    }
}
