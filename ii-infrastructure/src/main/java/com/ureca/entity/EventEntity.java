package com.ureca.entity;

import com.ureca.entity.Enum.EventStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Table(name = "event")
@Entity
public class EventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long eventId;

  private String name;

  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  private EventStatus status;

  @Builder
  public EventEntity(String name, String phoneNumber, EventStatus status) {
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.status = status;
  }
}
