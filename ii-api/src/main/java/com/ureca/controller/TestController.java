package com.ureca.controller;

import com.ureca.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/test")
@Controller
public class TestController {

  private static final Logger logger = LoggerFactory.getLogger(TestController.class);

  private TestService testService;

  public TestController(TestService testService) {
    this.testService = testService;
  }

  @GetMapping("/view")
  public String view() {
    testService.setTicketInfo("야이거다");
    logger.info("호출 성공!!!!");
    // UI 표준 - 임시 화면 테스트
    return "home";
  }
}
