package com.ureca.service;

import com.ureca.dto.BookInfo;
import com.ureca.dto.BookPage;
import com.ureca.entity.BookEntity;
import com.ureca.repository.BookRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RecommendService {

  private final BookRepository bookRepository;

  private BookPage<BookInfo> getBookPage(Long count, List<BookEntity> entities) {
    List<BookInfo> bookInfos = entities.stream()
        .map(entity -> new BookInfo(
            entity.getBookId(),
            entity.getBookName(),
            entity.getBookImgUrl(),
            entity.getWriter(),
            entity.getPublisher()))
        .collect(Collectors.toList());
    return new BookPage<>(count, bookInfos);
  }

  public BookPage<BookInfo> recommendSimilarBooks(Long childId, int offset, int limit) {
    Long count = bookRepository.countSimilarBooks(childId);

    if (count > 0) {
      List<BookEntity> entities = bookRepository.findSimilarBooks(childId, offset, limit);
      return getBookPage(count, entities);
    }
    return new BookPage<>(count, List.of());
  }

  public BookPage<BookInfo> recommendOppositeBooks(Long childId, int offset, int limit) {
    Long count = bookRepository.countOppositeBooks(childId);

    if (count > 0) {
      List<BookEntity> entities = bookRepository.findOppositeBooks(childId, offset, limit);
      return getBookPage(count, entities);
    }
    return new BookPage<>(count, List.of());
  }

  public BookPage<BookInfo> recommendSimilarChildLikedBooks(Long childId, int offset, int limit) {
    Long count = bookRepository.countSimilarChildLikedBooks(childId);

    if (count > 0) {
      List<BookEntity> entities = bookRepository.findSimilarChildLikedBooks(childId, offset, limit);
      return getBookPage(count, entities);
    }
    return new BookPage<>(count, List.of());
  }
}
