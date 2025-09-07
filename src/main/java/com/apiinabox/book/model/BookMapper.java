package com.apiinabox.book.model;

import com.apiinabox.book.api.dto.BookProto;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class BookMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

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

    public Book toModel(BookProto.Book bookProto) {
        if (bookProto == null) {
            return null;
        }
        return new Book(
            bookProto.getId(),
            bookProto.getTitle(),
            bookProto.getAuthor(),
            LocalDate.parse(bookProto.getPublishedDate(), DATE_FORMATTER)
        );
    }

    public BookProto.BookList toProtoList(java.util.List<Book> books) {
        BookProto.BookList.Builder builder = BookProto.BookList.newBuilder();
        books.forEach(book -> builder.addBooks(toProto(book)));
        return builder.build();
    }
} 