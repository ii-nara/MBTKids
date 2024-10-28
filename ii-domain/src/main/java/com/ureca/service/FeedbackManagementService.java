package com.ureca.service;

import com.ureca.dto.ResponseFeedbackDto;
import com.ureca.entity.BookEntity;
import com.ureca.entity.ChildEntity;
import com.ureca.entity.Enum.LikeStatus;
import com.ureca.entity.FeedbackStatusEntity;
import com.ureca.repository.BookRepository;
import com.ureca.repository.FeedbackStatusRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackManagementService {

  private final FeedbackStatusRepository feedbackStatusRepository;

  private final BookRepository bookRepository;

  public ResponseFeedbackDto addFeedbackStatus(BookEntity book, ChildEntity child, Integer likeStatusValue) {
    FeedbackStatusEntity feedbackStatus = feedbackStatusRepository
        .findByBookEntity_BookIdAndChildEntity_ChildId(book.getBookId(), child.getChildId())
        .orElseGet(
            () -> feedbackStatusRepository.save(FeedbackStatusEntity.builder()
            .childEntity(child)
            .bookEntity(book)
            .isLike(LikeStatus.CANCELED)
            .build()));

    int feedbackValue = updateFeedbackStatus(feedbackStatus, likeStatusValue);

    return ResponseFeedbackDto.of(book.getBookId(), feedbackValue, book.getTypeIE(),
        book.getTypeSN(), book.getTypeTF(), book.getTypePJ(), feedbackStatus.getIsLike());
  }

  public int updateFeedbackStatus(FeedbackStatusEntity feedbackStatus, Integer likeStatusValue) {
    int preFeedbackValue = feedbackStatus.getIsLike().equals(LikeStatus.CANCELED) ? 0 : feedbackStatus.getIsLike().equals(LikeStatus.LIKE) ? 1 : -1;

    if (preFeedbackValue == likeStatusValue) {
      feedbackStatus.updateLikeStatus(LikeStatus.CANCELED);
      return preFeedbackValue * -1;
    } else {
      if (likeStatusValue > preFeedbackValue) {
        feedbackStatus.updateLikeStatus(LikeStatus.LIKE);
        return 1;
      } else {
        feedbackStatus.updateLikeStatus(LikeStatus.DISLIKE);
        return -1;
      }
    }
  }

  public BookEntity getBookById(Long bookId) {
    return bookRepository.getReferenceById(bookId);
  }

  public String findFeedbackStatus(Long bookId, Long childId) {
    Optional<FeedbackStatusEntity> findFeedbackStatus = feedbackStatusRepository.findByBookEntity_BookIdAndChildEntity_ChildId(bookId, childId);
    return findFeedbackStatus.map(feedbackStatusEntity -> feedbackStatusEntity.getIsLike().name())
        .orElseGet(LikeStatus.CANCELED::name);
  }
}
