package com.ureca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mbti_info")
@Getter
@NoArgsConstructor
public class MbtiInfoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "mbtiId")
  private Integer mbtiId;

  @Column(name = "mbtiNm", length = 20, nullable = false)
  private String mbtiNm;

  @Column(name = "emoji", length = 20, nullable = false)
  private String emoji;

  @Column(name = "suffix", length = 45, nullable = false)
  private String suffix;

  @Column(name = "content", length = 200, nullable = false)
  private String content;

  @Column(name = "tag", length = 100, nullable = false)
  private String tag;

  @Builder
  public MbtiInfoEntity(String mbtiNm, String emoji, String suffix, String content, String tag) {
    this.mbtiNm = mbtiNm;
    this.emoji = emoji;
    this.suffix = suffix;
    this.content = content;
    this.tag = tag;
  }
}
