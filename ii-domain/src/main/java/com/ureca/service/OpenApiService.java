package com.ureca.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.ureca.entity.BookEntity;
import com.ureca.repository.BookRepository;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OpenApiService {

  private static final Logger logger = LoggerFactory.getLogger(OpenApiService.class);

  private BookRepository bookRepository;

  public OpenApiService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public void setBookList(JsonNode itemsNode) {

    for (JsonNode itemNode : itemsNode) {
      String title = itemNode.path("title").asText(); //도서명
      String description = itemNode.path("description").asText(); //줄거리
      String rights = itemNode.path("rights").asText(); //저자명

      // plot의 길이 확인
      if (description != null && description.length() > 1000) {
        // 필요한 경우, 길이를 자르거나 적절한 방법으로 처리
        description = description.substring(0, 1000);
      } else if (description == null) {
        description = ""; // 또는 다른 기본값을 설정할 수 있습니다.
      }

      // 도서명 필수
      if (title != null && title.length() < 100) {
        // 데이터 추가
        // 데이터 넣기
        BookEntity newBook = BookEntity.builder()
            .bookName(title)
            .bookImgUrl("http://file.koreafilm.or.kr/thm/02/99/18/52/tn_DPF029868.jpg")
            .plot(description)
            .writer(rights)
            .writerCd("W05")
            .publisher("미정")
            .publisherCd("P05")
            .recommenedAge("7세 이상")
            .typeIE(0)
            .typeSN(0)
            .typeTF(0)
            .typePJ(0)
            .createdAt(new Date())
            .displayYn("Y")
            .build();
        BookEntity savedBook = bookRepository.save(newBook);
        logger.info("데이터 등록 확인: {}", savedBook);
      }

    }

  }

}
