package com.ureca.service;

import com.ureca.dto.EventSaveRequestDto;
import jakarta.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EventService {

  public static final String KEY_PREFIX = "event:parentId:";
  public static final int ONE_DAY = 24;
  public static final String EXISTS = "exists";

  private final StringRedisTemplate redisTemplate;

  public boolean exist(Long parentId) {
    ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
    String key = KEY_PREFIX + parentId;
    return valueOps.get(key) != null;
  }

  @Resource(name = "eventRabbitTemplate")
  private final RabbitTemplate rabbitTemplate;

  public String eventApplication(Long parentId, EventSaveRequestDto eventSaveRequestDto) {
    if (exist(parentId)) {
      return "이미 응모하셨습니다.";
    }

    ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
    //    valueOps.set(KEY_PREFIX + parentId, EXISTS, Duration.ofHours(ONE_DAY));
    valueOps.set(KEY_PREFIX + parentId, EXISTS, Duration.ofMinutes(10L));

    rabbitTemplate.convertAndSend("eventExchange", "eventRoutingKey", eventSaveRequestDto);

    return "응모가 완료되었습니다.";
  }

  public boolean isEventTime() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime eventStartTime = LocalDateTime.of(
        now.getYear(), now.getMonth(), now.getDayOfMonth(), 19, 3);
    return now.isAfter(eventStartTime) || now.isEqual(eventStartTime);
  }
}
