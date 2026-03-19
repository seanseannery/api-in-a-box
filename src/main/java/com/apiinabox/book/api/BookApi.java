package com.apiinabox.book.api;

import com.apiinabox.book.api.dto.BookProto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST API contract for book CRUD operations. */
@RestController
@RequestMapping("/api/books")
public interface BookApi {
  @PostMapping
  @PreAuthorize("hasRole('ADMIN') ")
  public ResponseEntity<BookProto.Book> createBook(
      @RequestBody BookProto.Book bookProto, @AuthenticationPrincipal Jwt jwt);

  @GetMapping("/{id}")
  @PreAuthorize("isAuthenticated() or hasRole('READER')")
  public ResponseEntity<BookProto.Book> getBook(
      @PathVariable String id, @AuthenticationPrincipal Jwt jwt);

  @GetMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<BookProto.BookList> getAllBooks(@AuthenticationPrincipal Jwt jwt);

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') or hasRole('WRITER')")
  public ResponseEntity<BookProto.Book> updateBook(
      @PathVariable String id,
      @RequestBody BookProto.Book bookProto,
      @AuthenticationPrincipal Jwt jwt);

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteBook(@PathVariable String id, @AuthenticationPrincipal Jwt jwt);
}
