package com.apiinabox.book.api;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.apiinabox.book.api.dto.BookDTO;
@RestController
@RequestMapping("/api/books")
public interface BookApi {
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') ")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO, @AuthenticationPrincipal Jwt jwt);

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() or hasRole('READER')")
    public ResponseEntity<BookDTO> getBook(@PathVariable String id, @AuthenticationPrincipal Jwt jwt);

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookDTO>> getAllBooks(@AuthenticationPrincipal Jwt jwt);

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WRITER')")
    public ResponseEntity<BookDTO> updateBook(@PathVariable String id, @RequestBody BookDTO bookDTO, @AuthenticationPrincipal Jwt jwt);

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable String id, @AuthenticationPrincipal Jwt jwt);

}
