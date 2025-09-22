package com.example.ohmobackend.web.dto.groupDto;

import lombok.*;

public class GroupRequestDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddGroupRequestDto {
        private String groupName;
        private String groupCode;
        private String groupColor;
        private int numPeople;
        private String nickname;
    }
}
