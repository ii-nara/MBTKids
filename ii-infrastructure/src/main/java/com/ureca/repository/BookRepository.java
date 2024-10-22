package com.ureca.repository;

import com.ureca.entity.BookEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

  @Query(
      "FROM BookEntity b " +
          "WHERE b.bookName LIKE CONCAT('%', :searchWord, '%') " +
          "OR b.writer LIKE CONCAT('%', :searchWord, '%') " +
          "OR b.publisher LIKE CONCAT('%', :searchWord, '%')")
  List<BookEntity> findByBookNameOrWriterOrPublisher(@Param("searchWord") String searchWord);

  @Query(value = """
          SELECT b.*,
                 (CASE 
                     WHEN b.typeIE = 1 AND (m.typeIE BETWEEN 1 AND 5) THEN 1
                     WHEN b.typeIE = -1 AND (m.typeIE BETWEEN 6 AND 10) THEN 1
                     WHEN b.typeIE = 0 THEN 0
                     ELSE -1
                 END +
                 CASE 
                     WHEN b.typeSN = 1 AND (m.typeSN BETWEEN 1 AND 5) THEN 1
                     WHEN b.typeSN = -1 AND (m.typeSN BETWEEN 6 AND 10) THEN 1
                     WHEN b.typeSN = 0 THEN 0
                     ELSE -1
                 END +
                 CASE 
                     WHEN b.typeTF = 1 AND (m.typeTF BETWEEN 1 AND 5) THEN 1
                     WHEN b.typeTF = -1 AND (m.typeTF BETWEEN 6 AND 10) THEN 1
                     WHEN b.typeTF = 0 THEN 0
                     ELSE -1
                 END +
                 CASE 
                     WHEN b.typePJ = 1 AND (m.typePJ BETWEEN 1 AND 5) THEN 1
                     WHEN b.typePJ = -1 AND (m.typePJ BETWEEN 6 AND 10) THEN 1
                     WHEN b.typePJ = 0 THEN 0
                     ELSE -1
                 END) AS similarity
          FROM book b
          JOIN mbti_status m ON m.childId = :childId
          WHERE 
            b.displayYn = 'Y'
          AND
            (b.bookId NOT IN (SELECT f.bookId FROM feedback_status f WHERE f.childId = :childId AND f.isLike = -1))  -- 싫어요 피드백 체크
          AND
            ((b.typeIE = 1 AND m.typeIE <= 5) OR
            (b.typeIE = -1 AND m.typeIE >= 6) OR
            (b.typeSN = 1 AND m.typeSN <= 5) OR
            (b.typeSN = -1 AND m.typeSN >= 6) OR
            (b.typeTF = 1 AND m.typeTF <= 5) OR
            (b.typeTF = -1 AND m.typeTF >= 6) OR
            (b.typePJ = 1 AND m.typePJ <= 5) OR
            (b.typePJ = -1 AND m.typePJ >= 6))
          GROUP BY b.bookId
          ORDER BY similarity DESC
          LIMIT :offset, :limit
      """, nativeQuery = true)
  List<BookEntity> findSimilarBooks(Long childId, int offset, int limit);

  @Query(value = """
          SELECT b.*,
                 (CASE 
                     WHEN b.typeIE = -1 AND (m.typeIE BETWEEN 1 AND 5) THEN 1  -- E에 대한 반대
                     WHEN b.typeIE = 1 AND (m.typeIE BETWEEN 6 AND 10) THEN 1   -- I에 대한 반대
                     WHEN b.typeIE = 0 THEN 0
                     ELSE -1
                 END +
                 CASE 
                     WHEN b.typeSN = -1 AND (m.typeSN BETWEEN 1 AND 5) THEN 1  -- N에 대한 반대
                     WHEN b.typeSN = 1 AND (m.typeSN BETWEEN 6 AND 10) THEN 1   -- S에 대한 반대
                     WHEN b.typeSN = 0 THEN 0
                     ELSE -1
                 END +
                 CASE 
                     WHEN b.typeTF = -1 AND (m.typeTF BETWEEN 1 AND 5) THEN 1  -- F에 대한 반대
                     WHEN b.typeTF = 1 AND (m.typeTF BETWEEN 6 AND 10) THEN 1   -- T에 대한 반대
                     WHEN b.typeTF = 0 THEN 0
                     ELSE -1
                 END +
                 CASE 
                     WHEN b.typePJ = -1 AND (m.typePJ BETWEEN 1 AND 5) THEN 1  -- J에 대한 반대
                     WHEN b.typePJ = 1 AND (m.typePJ BETWEEN 6 AND 10) THEN 1   -- P에 대한 반대
                     WHEN b.typePJ = 0 THEN 0
                     ELSE -1
                 END) AS similarity
          FROM book b
          JOIN mbti_status m ON m.childId = :childId
          WHERE 
            b.displayYn = 'Y'
          AND
            (b.bookId NOT IN (SELECT f.bookId FROM feedback_status f WHERE f.childId = :childId AND f.isLike = -1))  -- 싫어요 피드백 체크
          AND
            ((b.typeIE = -1 AND m.typeIE <= 5) OR  -- E 성향인 경우
            (b.typeIE = 1 AND m.typeIE >= 6) OR   -- I 성향인 경우
            (b.typeSN = -1 AND m.typeSN <= 5) OR  -- N 성향인 경우
            (b.typeSN = 1 AND m.typeSN >= 6) OR   -- S 성향인 경우
            (b.typeTF = -1 AND m.typeTF <= 5) OR  -- F 성향인 경우
            (b.typeTF = 1 AND m.typeTF >= 6) OR   -- T 성향인 경우
            (b.typePJ = -1 AND m.typePJ <= 5) OR  -- P 성향인 경우
            (b.typePJ = 1 AND m.typePJ >= 6))      -- J 성향인 경우
          GROUP BY b.bookId
          ORDER BY similarity DESC
          LIMIT :offset, :limit
      """, nativeQuery = true)
  List<BookEntity> findOppositeBooks(Long childId, int offset, int limit);

  @Query(value = """
          SELECT b.*, 
                 COUNT(f.isLike) AS likeCount
          FROM book b
          JOIN feedback_status f ON f.bookId = b.bookId
          JOIN mbti_status m1 ON f.childId = m1.childId  -- 좋아요를 남긴 유저의 MBTI
          JOIN mbti_status m2 ON m2.childId = :childId   -- 쿼리에 전달된 자녀의 MBTI
          WHERE 
            b.displayYn = 'Y'
          AND
            (b.bookId NOT IN (SELECT f.bookId FROM feedback_status f WHERE f.childId = :childId AND f.isLike = -1))  -- 싫어요 피드백 체크
          AND
            ((m1.typeIE BETWEEN 1 AND 5 AND m2.typeIE BETWEEN 1 AND 5) OR 
             (m1.typeIE BETWEEN 6 AND 10 AND m2.typeIE BETWEEN 6 AND 10) OR
             (ABS(m1.typeIE - m2.typeIE) <= 2)) AND  -- 유사한 IE 성향
            ((m1.typeSN BETWEEN 1 AND 5 AND m2.typeSN BETWEEN 1 AND 5) OR 
             (m1.typeSN BETWEEN 6 AND 10 AND m2.typeSN BETWEEN 6 AND 10) OR
             (ABS(m1.typeSN - m2.typeSN) <= 2)) AND  -- 유사한 SN 성향
            ((m1.typeTF BETWEEN 1 AND 5 AND m2.typeTF BETWEEN 1 AND 5) OR 
             (m1.typeTF BETWEEN 6 AND 10 AND m2.typeTF BETWEEN 6 AND 10) OR
             (ABS(m1.typeTF - m2.typeTF) <= 2)) AND  -- 유사한 TF 성향
            ((m1.typePJ BETWEEN 1 AND 5 AND m2.typePJ BETWEEN 1 AND 5) OR 
             (m1.typePJ BETWEEN 6 AND 10 AND m2.typePJ BETWEEN 6 AND 10) OR
             (ABS(m1.typePJ - m2.typePJ) <= 2)) AND  -- 유사한 PJ 성향
            f.isLike = 1  -- 좋아요가 표시된 콘텐츠만
          GROUP BY b.bookId
          ORDER BY likeCount DESC
          LIMIT :offset, :limit
      """, nativeQuery = true)
  List<BookEntity> findSimilarChildLikedBooks(Long childId, int offset, int limit);
}
