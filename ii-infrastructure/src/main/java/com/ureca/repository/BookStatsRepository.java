package com.ureca.repository;

import com.ureca.entity.BookStatsEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookStatsRepository extends JpaRepository<BookStatsEntity, Long> {

  // 도서 통계 데이터 추가
  @Modifying
  @Query(
      value =
          """
        INSERT INTO book_stats (bookId, bookName, publisher, likeCnt, disLikeCnt, avgAge, avgMbti, statsAt)
        VALUES (
            :bookId,
            (SELECT bookName FROM book WHERE bookId = :bookId),
            (SELECT publisher FROM book WHERE bookId = :bookId),
            (SELECT COUNT(b.bookId) FROM book b JOIN feedback_status f ON b.bookId = f.bookId WHERE b.bookId = :bookId AND f.isLike = 'LIKE'),
            (SELECT COUNT(b.bookId) FROM book b JOIN feedback_status f ON b.bookId = f.bookId WHERE b.bookId = :bookId AND f.isLike = 'DISLIKE'),
            (SELECT FORMAT(AVG(c.childAge), 1) FROM book b JOIN feedback_status f ON b.bookId = f.bookId JOIN child_user c ON c.childId = f.childId WHERE b.bookId = :bookId),
            (SELECT CONCAT(
                CASE WHEN AVG(m.typeIE) BETWEEN 1 AND 5 THEN 'I' WHEN AVG(m.typeIE) BETWEEN 6 AND 10 THEN 'E' ELSE '_' END,
                CASE WHEN AVG(m.typeSN) BETWEEN 1 AND 5 THEN 'S' WHEN AVG(m.typeSN) BETWEEN 6 AND 10 THEN 'N' ELSE '_' END,
                CASE WHEN AVG(m.typeTF) BETWEEN 1 AND 5 THEN 'T' WHEN AVG(m.typeTF) BETWEEN 6 AND 10 THEN 'F' ELSE '_' END,
                CASE WHEN AVG(m.typePJ) BETWEEN 1 AND 5 THEN 'P' WHEN AVG(m.typePJ) BETWEEN 6 AND 10 THEN 'J' ELSE '_' END
            ) FROM book b JOIN feedback_status f ON b.bookId = f.bookId JOIN child_user c ON c.childId = f.childId JOIN mbti_status m ON c.childId = m.childId WHERE b.bookId = :bookId),
            :statsAt
        )
    """,
      nativeQuery = true)
  void insertBookStats(@Param("bookId") Long bookId, @Param("statsAt") LocalDate statsAt);

  // 시작일자와 종료일자, 출판사명, 도서명으로 조회
  @Query(
      "SELECT b FROM BookStatsEntity b WHERE b.statsAt >= :startDate AND b.statsAt <= :endDate "
          + "AND (:publisher IS NULL OR :publisher = '' OR b.publisher LIKE %:publisher%) "
          + "AND (:bookName IS NULL OR :bookName = '' OR b.bookName LIKE %:bookName%)")
  List<BookStatsEntity> findByStats(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("publisher") String publisher,
      @Param("bookName") String bookName);
}
