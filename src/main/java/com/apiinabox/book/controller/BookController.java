package com.apiinabox.book.controller;

import com.apiinabox.book.api.BookApi;
import com.apiinabox.book.api.dto.BookProto;
import com.apiinabox.book.model.Book;
import com.apiinabox.book.model.BookMapper;
import com.apiinabox.book.repository.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class BookController implements BookApi {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookController(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public ResponseEntity<BookProto.Book> createBook(BookProto.Book bookProto, @AuthenticationPrincipal Jwt jwt) {
        Book book = bookMapper.toModel(bookProto);
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(bookMapper.toProto(savedBook));
    }

    @Override
    public ResponseEntity<BookProto.Book> getBook(String id, @AuthenticationPrincipal Jwt jwt) {
        Book book = bookRepository.findById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookMapper.toProto(book));
    }

    @Override
    public ResponseEntity<BookProto.BookList> getAllBooks(@AuthenticationPrincipal Jwt jwt) {
        List<Book> books = bookRepository.findAll();
        BookProto.BookList.Builder builder = BookProto.BookList.newBuilder();
        books.stream()
                .map(bookMapper::toProto)
                .forEach(builder::addBooks);
        return ResponseEntity.ok(builder.build());
    }

    @Override

    public ResponseEntity<BookProto.Book> updateBook(String id, BookProto.Book bookProto, @AuthenticationPrincipal Jwt jwt) {
        Book book = bookMapper.toModel(bookProto);
        book.setId(id);
        Book updatedBook = bookRepository.update(id, book);
        if (updatedBook == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookMapper.toProto(updatedBook));
    }

    @Override

    public ResponseEntity<Void> deleteBook(String id, @AuthenticationPrincipal Jwt jwt) {
        bookRepository.delete(id);
        return ResponseEntity.noContent().build();
    }
}