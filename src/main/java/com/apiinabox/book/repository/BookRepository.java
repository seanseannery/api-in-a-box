package com.apiinabox.book.repository;

import com.apiinabox.book.model.Book;
import java.util.List;

/** Data access interface for Book persistence operations. */
public interface BookRepository {
  Book save(Book book);

  Book findById(String id);

  List<Book> findAll();

  Book update(String id, Book book);

  void delete(String id);
}
