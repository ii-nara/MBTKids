package com.ureca.repository;

import com.ureca.dto.BookInfo;
import com.ureca.entity.BookEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

  // 도서 목록 조회
  @Query(
      "SELECT new com.ureca.dto.BookInfo(b.bookId, b.bookName, b.bookImgUrl, b.writer, b.publisher) " +
          "FROM BookEntity b " +
          "WHERE b.bookName LIKE CONCAT('%', :searchWord, '%') " +
          "OR b.writer LIKE CONCAT('%', :searchWord, '%') " +
          "OR b.publisher LIKE CONCAT('%', :searchWord, '%')")
  List<BookInfo> findByBookNameOrWriterOrPublisher(@Param("searchWord") String searchWord);

  // 도서별 좋아요 개수
  @Query("SELECT COUNT(fs.feedbackId) FROM FeedbackStatusEntity fs " +
      "WHERE fs.bookEntity.bookId = :bookId AND fs.isLike = com.ureca.entity.Enum.LikeStatus.LIKE")
  int countLikesByBookId(@Param("bookId") Long bookId);

  // 도서별 싫어요 개수
  @Query("SELECT COUNT(fs.feedbackId) FROM FeedbackStatusEntity fs " +
      "WHERE fs.bookEntity.bookId = :bookId AND fs.isLike = com.ureca.entity.Enum.LikeStatus.DISLIKE")
  int countDislikesByBookId(@Param("bookId") Long bookId);

}
