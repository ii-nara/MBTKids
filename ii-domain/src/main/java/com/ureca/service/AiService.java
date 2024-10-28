package com.ureca.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AiService {
  private static final Logger logger = LoggerFactory.getLogger(AiService.class);

  public String setBookMbti(String contents) {

    String mbti = "0000";

    //ai 성향 분석 결과 내용 mbti에 넣기("ENTJ"), 해당없음은 0으로("0") 넣기 ex)EN0J
    return mbti;


  } //setBookMbti


}
