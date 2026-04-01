package com.example.ohmobackend.service;

import com.example.ohmobackend.domain.enums.FcmNotificationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class FcmNotificationEvent {

    private final List<String> tokens;
    private final String title;
    private final String body;
    private final FcmNotificationType type;
}
