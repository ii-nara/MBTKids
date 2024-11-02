package com.ureca.service;

import com.ureca.dto.EventSaveRequestDto;
import com.ureca.entity.EventEntity;
import com.ureca.repository.EventRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EventService {

  private final EventRepository eventRepository;

  @Resource(name = "eventRabbitTemplate")
  private final RabbitTemplate rabbitTemplate;

  @Transactional
  public void save(EventSaveRequestDto eventSaveRequestDto) {
    eventRepository.save(
        EventEntity.builder()
            .name(eventSaveRequestDto.getName())
            .phoneNumber(eventSaveRequestDto.getPhone())
            .build());
  }

  public void eventApplication(EventSaveRequestDto eventSaveRequestDto) {
    rabbitTemplate.convertAndSend("eventExchange", "eventRoutingKey", eventSaveRequestDto);
  }
}
