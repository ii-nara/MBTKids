package com.ureca.repository;

import com.ureca.entity.BookEntity;
import java.awt.print.Book;
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

}
