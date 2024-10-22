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
            (b.typeIE = 1 AND m.typeIE <= 5) OR
            (b.typeIE = -1 AND m.typeIE >= 6) OR
            (b.typeSN = 1 AND m.typeSN <= 5) OR
            (b.typeSN = -1 AND m.typeSN >= 6) OR
            (b.typeTF = 1 AND m.typeTF <= 5) OR
            (b.typeTF = -1 AND m.typeTF >= 6) OR
            (b.typePJ = 1 AND m.typePJ <= 5) OR
            (b.typePJ = -1 AND m.typePJ >= 6)
          GROUP BY b.bookId
          ORDER BY similarity DESC
      """, nativeQuery = true)
  List<BookEntity> findSimilarBooks(@Param("childId") Long childId);
}
