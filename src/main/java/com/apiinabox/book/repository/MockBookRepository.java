package com.apiinabox.book.repository;

import com.apiinabox.book.model.Book;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MockBookRepository implements BookRepository {
    private final List<Book> books = new ArrayList<>();

    public MockBookRepository() {
        // Initialize with some example books
        books.add(new Book("1", "The Great Gatsby", "F. Scott Fitzgerald", LocalDate.of(1925, 4, 10)));
        books.add(new Book("2", "To Kill a Mockingbird", "Harper Lee", LocalDate.of(1960, 7, 11)));
    }

    @Override
    public Book save(Book book) {
        books.add(book);
        return book;
    }

    @Override
    public Book findById(String id) {
        return books.isEmpty() ? null : books.get(0);
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    @Override
    public Book update(String id, Book book) {
        return book;
    }

    @Override
    public void delete(String id) {
        if (!books.isEmpty()) {
            books.remove(0);
        }
    }
} 