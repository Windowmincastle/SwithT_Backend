package com.tweety.SwithT.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
//@RequiredArgsConstructor
public class RedisStreamProducer {

    @Autowired
    @Qualifier("4")
    private RedisTemplate<String, Object> redisTemplate;

    private static final String STREAM_NAME = "sse-notifications";
    private static final String WAITINGSTREAM_NAME = "waiting-notifications";

    // 스트림 이름을 매개변수로 받는 메서드
    public RecordId publishDefaultMessage(String streamName, String memberId, String messageType, String title, String contents) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("memberId", memberId);
        messageMap.put("messageType", messageType);
        messageMap.put("title", title);
        messageMap.put("contents", contents);

        ObjectRecord<String, Map<String, String>> record = StreamRecords.newRecord()
                .in(streamName)
                .ofObject(messageMap);
        return redisTemplate.opsForStream().add(record);
    }

    // 기본 스트림을 사용하는 메서드
    public RecordId publishMessage(String memberId, String messageType, String title, String contents) {
        return publishDefaultMessage(STREAM_NAME, memberId, messageType, title, contents);
    }

    // 대기열 스트림을 사용하는 메서드
    public RecordId publishWaitingMessage(String memberId, String messageType, String title, String contents) {
        return publishDefaultMessage(WAITINGSTREAM_NAME, memberId, messageType, title, contents);
    }
}
