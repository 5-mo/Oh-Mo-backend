package com.example.ohmobackend.web.dto.groupDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class GroupResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupDto{
        private Long groupId;
        private String groupName;
        private String groupCode;
        private String groupColor;
        private int numPeople;
    }
}
