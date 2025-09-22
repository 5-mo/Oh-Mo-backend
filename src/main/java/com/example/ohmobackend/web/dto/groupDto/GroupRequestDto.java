package com.example.ohmobackend.web.dto.groupDto;

import lombok.Getter;
import lombok.Setter;

public class GroupRequestDto {

    @Setter
    @Getter
    public static class AddGroupRequestDto {
        private String groupName;
        private String groupCode;
        private String groupColor;
        private int numPeople;
        private String nickname;
    }
}
