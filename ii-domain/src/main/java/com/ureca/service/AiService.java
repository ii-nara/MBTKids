package com.ureca.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.Enum.MbtiType;
import com.ureca.entity.BookEntity;
import com.ureca.repository.BookRepository;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AiService {

  private static final Logger logger = LoggerFactory.getLogger(AiService.class);
  private final RestTemplate restTemplate;
  private final BookRepository bookRepository;

  // Lambda REST API URL
  @Value("${api.lambda.url}")
  private String lambdaUrl;

  public AiService(RestTemplate restTemplate, BookRepository bookRepository) {
    this.restTemplate = restTemplate;
    this.bookRepository = bookRepository;
  }

  // [1] 도서 책 기반 MBTI 성향 API 요청 및 응답
  public String apiBookMbti(String title, String contents) {
    // 책 제목, 줄거리 입력
    Map<String, String> request = new HashMap<>();
    request.put("title", title);
    request.put("contents", contents);
    // Lambda REST API 요청
    ResponseEntity<String> response = restTemplate.postForEntity(lambdaUrl, request, String.class);
    logger.info("응답: {}", response.getBody());
    return parseMbtiFromResponse(response.getBody());
  }

  // [2] MBTI 응답 파싱 및 추출
  private String parseMbtiFromResponse(String responseBody) {
    String mbti = "";
    try {
      // 전체 응답 파싱
      ObjectMapper objectMapper = new ObjectMapper();
      Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
      // body 파싱
      String bodyJson = (String) responseMap.get("body");
      Map<String, Object> bodyMap = objectMapper.readValue(bodyJson, Map.class);
      // mbti 응답 값
      mbti = bodyMap.get("mbti").toString();
    } catch (Exception e) {
      logger.error("Error parsing MBTI from response", e);
    }
    logger.info("mbti: {}", mbti);
    return mbti;
  }

  // [3] MBTI 성향 DB 업데이트
  public void updateBookMbti(Long userId, Long bookId, String mbti) {
    // 존재하는 도서 정보인지 확인
    Optional<BookEntity> optionalBook = bookRepository.findById(bookId);
    optionalBook.ifPresentOrElse(
        originBook -> {
          BookEntity updateBook = createUpdateBookEntity(originBook, mbti, userId);
          bookRepository.save(updateBook);
        },
        () -> logger.warn("Book not found with ID: {}", bookId));
  }

  private BookEntity createUpdateBookEntity(BookEntity originBook, String mbti, Long userId) {
    return BookEntity.builder()
        .bookId(originBook.getBookId())
        .bookName(originBook.getBookName())
        .bookImgUrl(originBook.getBookImgUrl())
        .plot(originBook.getPlot())
        .writer(originBook.getWriter())
        .publisher(originBook.getPublisher())
        .recommenedAge(originBook.getRecommenedAge())
        .typeIE(MbtiType.TYPE_IE.getValueForType(mbti.charAt(0) + ""))
        .typeSN(MbtiType.TYPE_SN.getValueForType(mbti.charAt(1) + ""))
        .typeTF(MbtiType.TYPE_TF.getValueForType(mbti.charAt(2) + ""))
        .typePJ(MbtiType.TYPE_PJ.getValueForType(mbti.charAt(3) + ""))
        .createdAt(originBook.getCreatedAt())
        .createId(originBook.getCreateId())
        .updateAt(new Date())
        .updateId(userId)
        .displayYn(originBook.getDisplayYn())
        .build();
  }
}
