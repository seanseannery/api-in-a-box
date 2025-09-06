package com.apiinabox.book.model;

import com.apiinabox.book.api.dto.BookDTO;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class BookMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public BookDTO toDTO(Book book) {
        if (book == null) {
            return null;
        }
        return BookDTO.builder()
            .id(book.getId())
            .title(book.getTitle())
            .author(book.getAuthor())
            .publishedDate(book.getPublishedDate().format(DATE_FORMATTER))
            .build();
    }

    public Book toModel(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        }
        return new Book(
            bookDTO.getId(),
            bookDTO.getTitle(),
            bookDTO.getAuthor(),
            LocalDate.parse(bookDTO.getPublishedDate(), DATE_FORMATTER)
        );
    }

} 