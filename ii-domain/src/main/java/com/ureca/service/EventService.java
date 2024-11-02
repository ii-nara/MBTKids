package com.ureca.service;

import com.ureca.dto.EventSaveRequestDto;
import com.ureca.entity.EventEntity;
import com.ureca.repository.EventRepository;
import java.time.Duration;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EventService {

  public static final String KEY_PREFIX = "event:parentId:";
  public static final int ONE_DAY = 24;
  public static final String EXISTS = "exists";

  private final EventRepository eventRepository;

  private final StringRedisTemplate redisTemplate;

  public boolean exist(Long parentId) {
    ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
    String key = KEY_PREFIX + parentId;
    return valueOps.get(key) != null;
  }

  @Resource(name = "eventRabbitTemplate")
  private final RabbitTemplate rabbitTemplate;

  @Transactional
  public String save(Long parentId, EventSaveRequestDto eventSaveRequestDto) {
    if (exist(parentId)) {
      return "이미 응모하셨습니다.";
    }

    ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
    valueOps.set(KEY_PREFIX + parentId, EXISTS, Duration.ofHours(ONE_DAY));

    eventRepository.save(
        EventEntity.builder()
            .name(eventSaveRequestDto.getName())
            .phoneNumber(eventSaveRequestDto.getPhone())
            .build());

    return "응모가 완료되었습니다.";
  }

  public void eventApplication(EventSaveRequestDto eventSaveRequestDto) {
    rabbitTemplate.convertAndSend("eventExchange", "eventRoutingKey", eventSaveRequestDto);
  }
}
