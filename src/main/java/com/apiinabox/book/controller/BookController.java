package com.apiinabox.book.controller;

import com.apiinabox.book.api.BookApi;
import com.apiinabox.book.api.dto.BookDTO;
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
    public ResponseEntity<BookDTO> createBook(BookDTO bookProto, @AuthenticationPrincipal Jwt jwt) {
        Book book = bookMapper.toModel(bookProto);
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(bookMapper.toDTO(savedBook));
    }

    @Override
    public ResponseEntity<BookDTO> getBook(String id, @AuthenticationPrincipal Jwt jwt) {
        Book book = bookRepository.findById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookMapper.toDTO(book));
    }

    @Override
    public ResponseEntity<List<BookDTO>> getAllBooks(@AuthenticationPrincipal Jwt jwt) {
        List<Book> books = bookRepository.findAll();
        List<BookDTO> bookDTOs = books.stream()
                .map(bookMapper::toDTO)
                .toList();
        return ResponseEntity.ok(bookDTOs);
    }



    @Override
    public ResponseEntity<BookDTO> updateBook(String id, BookDTO bookProto, @AuthenticationPrincipal Jwt jwt) {
        Book book = bookMapper.toModel(bookProto);
        book.setId(id);
        Book updatedBook = bookRepository.update(id, book);
        if (updatedBook == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookMapper.toDTO(updatedBook));
    }

    @Override

    public ResponseEntity<Void> deleteBook(String id, @AuthenticationPrincipal Jwt jwt) {
        bookRepository.delete(id);
        return ResponseEntity.noContent().build();
    }
}