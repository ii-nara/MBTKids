package com.ureca.service;

import com.ureca.dto.BookInfo;
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

  public List<BookInfo> recommendSimilarBooks(Long childId, int offset, int limit) {
    List<BookEntity> books = bookRepository.findSimilarBooks(childId, offset, limit);
    return books.stream()
        .map(book -> new BookInfo(
            book.getBookId(),
            book.getBookName(),
            book.getImgURL(),
            book.getWriter(),
            book.getWriterCd(),
            book.getPublisher(),
            book.getPublisherCd()))
        .collect(Collectors.toList());
  }

  public List<BookInfo> recommendOppositeBooks(Long childId, int offset, int limit) {
    List<BookEntity> books = bookRepository.findOppositeBooks(childId, offset, limit);
    return books.stream()
        .map(book -> new BookInfo(
            book.getBookId(),
            book.getBookName(),
            book.getImgURL(),
            book.getWriter(),
            book.getWriterCd(),
            book.getPublisher(),
            book.getPublisherCd()))
        .collect(Collectors.toList());
  }

  public List<BookInfo> recommendSimilarChildLikedBooks(Long childId, int offset, int limit) {
    List<BookEntity> books = bookRepository.findSimilarChildLikedBooks(childId, offset, limit);
    return books.stream()
        .map(book -> new BookInfo(
            book.getBookId(),
            book.getBookName(),
            book.getImgURL(),
            book.getWriter(),
            book.getWriterCd(),
            book.getPublisher(),
            book.getPublisherCd()))
        .collect(Collectors.toList());
  }
}
