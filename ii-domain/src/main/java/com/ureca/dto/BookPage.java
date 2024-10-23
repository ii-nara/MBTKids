package com.ureca.dto;

import java.util.List;

public class BookPage<T> {

  private final Long count;
  private final List<T> items;

  public BookPage(Long count, List<T> items) {
    this.count = count;
    this.items = items;
  }

  public Long getCount() {
    return count;
  }

  public List<T> getItems() {
    return items;
  }

  public boolean isNextPage(int page, int size) {
    return ((long) (page + 1) * size) < count;
  }
}
