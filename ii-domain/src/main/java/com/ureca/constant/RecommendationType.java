package com.ureca.constant;

import com.ureca.dto.BookInfo;
import com.ureca.dto.BookPage;
import com.ureca.service.RecommendService;
import lombok.Getter;

@Getter
public enum RecommendationType {
  SIMILAR("아이 성향 추천",
      (service, childId, page, size) -> service.recommendSimilarBooks(childId,
          page * size, size)),
  OPPOSITE("반대 성향 추천",
      (service, childId, page, size) -> service.recommendOppositeBooks(childId,
          page * size, size)),
  LIKE("유사 성향 아이들 추천",
      (service, childId, page, size) -> service.recommendSimilarChildLikedBooks(
          childId,
          page * size, size));

  private final String title;
  private final RecommendAction action;

  RecommendationType(String title, RecommendAction action) {
    this.title = title;
    this.action = action;
  }

  public BookPage<BookInfo> recommend(RecommendService service, Long childId, int page, int size) {
    return action.recommend(service, childId, page, size);
  }

  @FunctionalInterface
  public interface RecommendAction {

    BookPage<BookInfo> recommend(RecommendService service, Long childId, int page, int size);
  }
}
