package com.magenic.masters.item;

import java.util.stream.Stream;

public enum Category {

    PANTRY("Pantry"),
    MPS("Meat/Poultry/Seafood"),
    SNACKS("Snacks");

    private String description;
    Category (String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Category getByDescription(String description) {
        return Stream.of(Category.values())
                .filter(category -> category.getDescription().equals(description))
                .findFirst()
                .orElse(null);
    }
}
