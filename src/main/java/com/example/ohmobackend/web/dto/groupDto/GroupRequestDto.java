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
        private String groupPassword;
        private String groupColor;
        private int numPeople;
        private String nickname;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnterGroupRequestDto {
        private String groupCode;
        private String groupPassword;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddGroupNicknameDto {
        private Long groupId;
        private String nickname;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteGroupRequestDto {
        private Long groupId;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeaveGroupRequestDto {
        private Long groupId;
    }

}
