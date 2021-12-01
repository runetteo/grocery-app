package com.magenic.masters.item;

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
}
