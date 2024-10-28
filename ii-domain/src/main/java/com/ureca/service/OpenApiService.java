package com.ureca.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.ureca.Enum.MbtiType;
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
  private AiService aiService;

  public OpenApiService(BookRepository bookRepository, AiService aiService) {
    this.bookRepository = bookRepository;
    this.aiService = aiService;
  }

  public void setBookList(JsonNode documents) {

    // 도서 내용 추출
    for (JsonNode document : documents) {
      String title = document.get("title").asText(); // 도서명
      String contents = document.get("contents").asText(); // 줄거리
      String authors = document.get("authors").toString().replaceAll("[\\[\\]\"]", ""); // 저자 - 여러명
      String publisher = document.get("publisher").asText(); // 출판사
      String thumbnail = document.get("thumbnail").asText(); // 이미지 URL

      // 줄거리 길이 확인
      if (contents != null && contents.length() > 1000) {
        // 필요한 경우, 길이를 자르거나 적절한 방법으로 처리
        contents = contents.substring(0, 1000);
      } else if (contents == null) {
        contents = ""; // 또는 다른 기본값을 설정할 수 있습니다.
      }

      // AI 성향 분석 Service 실행
      String textPJ ="", textTF="", textSN="",textIE="";
      String mbti = aiService.setBookMbti(contents);

      if(!mbti.isEmpty()){
        textIE = String.valueOf(mbti.charAt(0)); // I/E/0
        textSN = String.valueOf(mbti.charAt(1)); // S/N/0
        textTF = String.valueOf(mbti.charAt(2)); // T/F/0
        textPJ = String.valueOf(mbti.charAt(3)); // P/J/0
      }

      // 도서명 필수
      if (title != null && title.length() < 100) {
        // 데이터 추가
        BookEntity newBook = BookEntity.builder()
            .bookName(title)
            .bookImgUrl(thumbnail)
            .plot(contents)
            .writer(authors)
            .publisher(publisher)
            .recommenedAge("7세 이상")
            .typeIE(MbtiType.TYPE_IE.getValueForType(textIE))
            .typeSN(MbtiType.TYPE_SN.getValueForType(textSN))
            .typeTF(MbtiType.TYPE_TF.getValueForType(textTF))
            .typePJ(MbtiType.TYPE_PJ.getValueForType(textPJ))
            .createdAt(new Date())
            .displayYn("Y")
            .build();
        BookEntity savedBook = bookRepository.save(newBook);
        logger.info("데이터 등록 확인: {}", savedBook);
      }else{
        logger.info("도서명 필수, 100자 이하 : ", title);
      }

    }

  }

}
