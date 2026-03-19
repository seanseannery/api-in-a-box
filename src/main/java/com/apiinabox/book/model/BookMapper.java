package com.apiinabox.book.model;

import com.apiinabox.book.api.dto.BookProto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

/** Maps between Book domain model and BookProto protobuf representation. */
@Component
public class BookMapper {
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

  /** Converts a Book domain model to its protobuf representation. */
  public BookProto.Book toProto(Book book) {
    if (book == null) {
      return null;
    }
    return BookProto.Book.newBuilder()
        .setId(book.id())
        .setTitle(book.title())
        .setAuthor(book.author())
        .setPublishedDate(book.publishedDate().format(DATE_FORMATTER))
        .build();
  }

  /** Converts a BookProto protobuf message to a Book domain model. */
  public Book toModel(BookProto.Book bookProto) {
    if (bookProto == null) {
      return null;
    }
    return new Book(
        bookProto.getId(),
        bookProto.getTitle(),
        bookProto.getAuthor(),
        LocalDate.parse(bookProto.getPublishedDate(), DATE_FORMATTER));
  }

  /** Converts a list of Book domain models to a BookProto.BookList protobuf message. */
  public BookProto.BookList toProtoList(java.util.List<Book> books) {
    BookProto.BookList.Builder builder = BookProto.BookList.newBuilder();
    books.forEach(book -> builder.addBooks(toProto(book)));
    return builder.build();
  }
}
