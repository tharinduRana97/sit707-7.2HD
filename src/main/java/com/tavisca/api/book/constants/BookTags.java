package com.tavisca.api.book.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum BookTags {
    MILK("milk"),
    SUGAR("sugar"),
    SODA("soda"),
    MINT("mint"),
    WATER("water");

    private final String item;
    
    BookTags(String item) {
        this.item = item;
    }

    public static BookTags getItem(String beverage) {
        for (BookTags menuItem : BookTags.values()) {
            if (menuItem.item.equalsIgnoreCase(beverage)) {
                return menuItem;
            }
        }
        return null;
    }
}
