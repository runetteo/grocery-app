package com.magenic.masters;

import com.magenic.masters.item.Category;
import com.magenic.masters.item.Item;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class GroceryApp {

    private static final String MAIN_MENU = """
                Category:
                    1 - Pantry supplies
                    2 - Meat/Poultry/Seafood
                    3 - Snacks
                    
                Choose Category(-1 to Checkout, -2 to Exit):
                """;

    private static final String ITEM_FORMAT = "[%d]%s %10s/%s";

    private Scanner scanner;
    private List<Item> itemsInCart;
    private List<Item> items;

    public GroceryApp(List<Item> items) {
        this.items = items;
        this.scanner = new Scanner(System.in);
        this.itemsInCart = new ArrayList<>();
    }

    private int getValidIntInput(String message) {
        int userInput = 0;
        boolean isValid = false;
        do {
            try {
                System.out.print(message);
                userInput = input.nextInt();
                isValid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.\n");
            }
            input.nextLine();
        } while (!isValid);

        return userInput;
    }

    private void displayMainMenu() {
        int choice = getValidIntInput(MAIN_MENU)
        switch (choice) {
            case 1 -> {
                System.out.println("Pantry Items:");
                displayItems(Category.PANTRY);
            }
            case 2 -> {
                System.out.println("Meat/Poultry/Seafood Items:");
                displayItems(Category.MPS);
            }
            case 3 -> {
                System.out.println("Snack Items:");
                displayItems(Category.SNACKS);
            }
            case -1 -> System.out.println("checkout");
            case -2 -> {
                System.out.println("Unsupported option: Program Exiting now...");
            }
            default -> {
                System.out.println("Invalid input. Try again.");
                displayMainMenu();
            }
        }
    }

    private void displayItems(Category category) {
        AtomicInteger id = new AtomicInteger();
        items.stream()
                .filter(item -> item.getCategory() == category)
                .forEach(item -> {
                    System.out.println(ITEM_FORMAT.formatted(id.getAndIncrement(), item.getName(), item.getPrice(), item.getUnit()));
                });
    }

    public static void main(String[] args) {

        NumberFormat formatter = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        formatter.setMinimumFractionDigits(2);

        List<Item> items = new ArrayList<>();
        try {
            Path filepath = Paths.get("C:/Users/RunetteO/workspace/stocks.csv");
            String content = Files.readString(filepath);

            content.lines().filter(Predicate.not(String::isBlank)).forEach((var line) -> {
                String[] details = line.split(",");
                Item item = new Item(details[0], Double.valueOf(details[1]), details[2], Category.getByDescription(details[3])); //validation?
                items.add(item);
            });

            GroceryApp app = new GroceryApp(items);
            app.displayMainMenu();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
