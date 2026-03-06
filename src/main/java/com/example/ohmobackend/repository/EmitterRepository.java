package com.example.ohmobackend.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class EmitterRepository {
    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public void save(Long groupId, SseEmitter emitter) {
        emitters.computeIfAbsent(groupId, k -> new CopyOnWriteArrayList<>()).add(emitter);
    }

    public List<SseEmitter> findAllByGroupId(Long groupId) {
        return emitters.getOrDefault(groupId, new ArrayList<>());
    }

    public void delete(Long groupId, SseEmitter emitter) {
        List<SseEmitter> groupEmitters = emitters.get(groupId);
        if (groupEmitters != null) {
            groupEmitters.remove(emitter);
            if (groupEmitters.isEmpty()) {
                emitters.remove(groupId);
            }
        }
    }
}