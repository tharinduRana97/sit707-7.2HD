package com.tavisca.api.book.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum BookGenre {
    FICTION("Fiction"),
    NON_FICTION("Non-Fiction"),
    MYSTERY("Mystery"),
    BIOGRAPHY("Biography"),
    SCIENCE("Science"),
    FANTASY("Fantasy");

    private final String genre;
    
    BookGenre(String genre) {
        this.genre = genre;
    }

    public static BookGenre getGenre(String name) {
        for (BookGenre bookGenre : BookGenre.values()) {
            if (bookGenre.genre.equalsIgnoreCase(name)) {
                return bookGenre;
            }
        }
        return null;
    }
}