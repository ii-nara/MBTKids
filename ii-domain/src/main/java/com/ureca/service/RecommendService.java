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

  public List<BookInfo> recommendSimilarity(Long childId) {
    List<BookEntity> books = bookRepository.findSimilarBooks(childId);
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
