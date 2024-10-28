package com.ureca.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.service.OpenApiService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mbtkids")
public class BookController {

  private static final Logger logger = LoggerFactory.getLogger(BookController.class);

  private static final int SUCCESS = 0;
  private static final int APPLICATION_ERROR = 100;
  private static final int BAD_REQUEST = 401;
  private static final int FORBIDDEN = 403;
  private static final int NOT_FOUND = 404;
  private static final int METHOD_NOT_ALLOWED = 405;
  private static final int NOT_FOUNT_USER = -10;
  private static final int INVALID_PASSWORD = -20;
  private static final int ACCESS_DENIED = -30;
  private static final int SYSTEM_ERROR = -500;

  // application.properties에서 읽어옴
  @Value("${api.kcisa.serviceKey}")
  private String serviceKey;

  @Value("${api.kcisa.url}")
  private String apiUrl;

  @Value("${api.kcisa.target}")
  private String apiTarget;

  @Value("${api.kcisa.size}")
  private int apiSize;

  @Value("${api.kcisa.page}")
  private int apiPage;

  private OpenApiService openApiService;

  public BookController(OpenApiService openApiService) {
    this.openApiService = openApiService;
  }

  /**
   * @title 카카오 도서 검색 API
   * @description 카카오 도서 검색 API에서 전체 데이터를 검색합니다. 응답 결과에서 원하는 데이터를 JSON 형태로 변환하여 가져옵니다.
   */
  // http://localhost:8080/mbtkids/openapi/data
  @GetMapping("/openapi/data")
  public void getOpenApiData() throws IOException {
    try {
      // 요청 데이터
      String authorizationKey = serviceKey; // 인증키
      String target = apiTarget; // 검색 기준
      String query = "책읽는곰"; // 검색 내용
      String encodedQuery = URLEncoder.encode(query, "UTF-8");
      int size = apiSize; // 한 페이지 데이터 수
      int page = apiPage; // 페이지 수

      // URL 세팅
      String openApiUrl = apiUrl; // 오픈 API URL
      String urlText =
          openApiUrl
              + "?target="
              + target
              + "&query="
              + encodedQuery
              + "&size="
              + size
              + "&page="
              + page;
      URL url = new URL(urlText);

      // 통신 세팅
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Content-type", "application/json");
      conn.setRequestProperty("Accept", "application/json");
      conn.setRequestProperty("Authorization", "KakaoAK " + authorizationKey);

      // 응답 코드 확인
      // System.out.println("Response code: " + conn.getResponseCode());
      int responseCode = conn.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          sb.append(inputLine);
        }
        in.close();

        // JSON 응답 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(sb.toString());

        // 메타 데이터 확인
        JsonNode meta = rootNode.get("meta");
        int total_count = meta.get("total_count").asInt(); // 검색된 문서 수
        int pageable_count = meta.get("pageable_count").asInt(); // 노출 가능 문서 수
        int is_end = meta.get("is_end").asInt(); // 마지막 페이지 여부

        // service 호출
        JsonNode documents = rootNode.get("documents");
        openApiService.setBookList(documents);

      } else {
        System.out.println("GET request failed: " + responseCode);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  } // getOpenApiData
}
