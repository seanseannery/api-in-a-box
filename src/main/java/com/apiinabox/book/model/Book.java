package com.apiinabox.book.model;

import java.time.LocalDate;


public record Book(String id, String title, String author, LocalDate publishedDate) {
}
