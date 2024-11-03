package com.ureca.config.batch;

import com.ureca.service.BookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BookStatsScheduler {

  @Autowired private BookService bookService;

  /** 매일 새벽 2시에 도서 통계 등록 */
  @Scheduled(cron = "0 0 2 * * ?")
  public void scheduleBookStatistics() {
    // 모든 도서 ID를 조회
    List<Long> bookIdList = bookService.getBookIdList();
    // 각 도서 ID에 대해 통계 정보 저장
    for (Long bookId : bookIdList) bookService.saveBookStatistics(bookId);
  }
}
