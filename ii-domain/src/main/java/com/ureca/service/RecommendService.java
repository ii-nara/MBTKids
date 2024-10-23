package com.ureca.service;

import com.ureca.dto.BookInfo;
import com.ureca.dto.BookPage;
import com.ureca.entity.BookEntity;
import com.ureca.repository.BookRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RecommendService {

  private final BookRepository bookRepository;

  public BookPage<BookInfo> recommendSimilarBooks(Long childId, int offset, int limit) {
    Long count = bookRepository.countSimilarBooks(childId);

    List<BookInfo> bookInfos = new ArrayList<>();
    if (count > 0) {
      List<BookEntity> entities = bookRepository.findSimilarBooks(childId,
          offset, limit);
      bookInfos = entities.stream()
          .map(entity -> new BookInfo(
              entity.getBookId(),
              entity.getBookName(),
              entity.getImgURL(),
              entity.getWriter(),
              entity.getWriterCd(),
              entity.getPublisher(),
              entity.getPublisherCd()))
          .collect(Collectors.toList());
    }
    return new BookPage<>(count, bookInfos);
  }

  public BookPage<BookInfo> recommendOppositeBooks(Long childId, int offset, int limit) {
    Long count = bookRepository.countOppositeBooks(childId);

    List<BookInfo> bookInfos = new ArrayList<>();
    if (count > 0) {
      List<BookEntity> entities = bookRepository.findOppositeBooks(childId,
          offset, limit);
      bookInfos = entities.stream()
          .map(entity -> new BookInfo(
              entity.getBookId(),
              entity.getBookName(),
              entity.getImgURL(),
              entity.getWriter(),
              entity.getWriterCd(),
              entity.getPublisher(),
              entity.getPublisherCd()))
          .collect(Collectors.toList());
    }
    return new BookPage<>(count, bookInfos);
  }

  public BookPage<BookInfo> recommendSimilarChildLikedBooks(Long childId, int offset, int limit) {
    Long count = bookRepository.countSimilarChildLikedBooks(childId);

    List<BookInfo> bookInfos = new ArrayList<>();
    if (count > 0) {
      List<BookEntity> entities = bookRepository.findSimilarChildLikedBooks(childId,
          offset, limit);
      bookInfos = entities.stream()
          .map(entity -> new BookInfo(
              entity.getBookId(),
              entity.getBookName(),
              entity.getImgURL(),
              entity.getWriter(),
              entity.getWriterCd(),
              entity.getPublisher(),
              entity.getPublisherCd()))
          .collect(Collectors.toList());
    }
    return new BookPage<>(count, bookInfos);
  }
}
