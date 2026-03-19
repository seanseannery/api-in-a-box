package com.apiinabox.book.model;

import java.time.LocalDate;

/** Domain model representing a book. */
public record Book(String id, String title, String author, LocalDate publishedDate) {}
