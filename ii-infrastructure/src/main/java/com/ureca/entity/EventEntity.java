package com.ureca.entity;

import jakarta.persistence.Entity;
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

  @Builder
  public EventEntity(String name, String phoneNumber) {
    this.name = name;
    this.phoneNumber = phoneNumber;
  }
}
