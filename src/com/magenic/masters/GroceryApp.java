package com.magenic.masters;

import com.magenic.masters.item.Category;
import com.magenic.masters.item.Item;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GroceryApp {

    private static final String MAIN_MENU = """
                Category:
                    1 - Pantry supplies
                    2 - Meat/Poultry/Seafood
                    3 - Snacks
                    
                Choose Category(-1 to Checkout, -2 to Exit):""";
    
    private static final String ITEM_CART = """
            \nChoose item (-1 to go back to Categories):""";
    
    private static final String ITEM_QTY = """
            Enter how many:""";

    private static final String ITEM_QTY_KG = "Enter how much(in kg):";
    
    private static final String ITEM_FORMAT = "[%d]%-55s price: %s/%s";
    private static final String CART_ITEM_FORMAT = "%s | %s/%s x %s | %s";
    private static final String FILE_DIR = "resources/";
    private static final String FILENAME = "stocks.csv";

    private Scanner scanner;
    private List<Item> itemsInCart;
    private List<Item> allItems;
    private List<Item> itemsInCategory;
    private NumberFormat priceFrmtter;

    public GroceryApp(List<Item> items) {
        this.allItems = items;
        this.scanner = new Scanner(System.in);
        this.itemsInCart = new ArrayList<>();

        this.priceFrmtter = new DecimalFormat("#0.00");
    }

    private int getValidIntInput(String message) {
        int userInput = 0;
        boolean isValid = false;
        do {
            try {
                System.out.print(message);
                userInput = scanner.nextInt();
                isValid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
            scanner.nextLine();
        } while (!isValid);

        return userInput;
    }

    private double getValidDoubleInput(String message) {
        double userInput = 0;
        boolean isValid = false;
        do {
            try {
                System.out.print(message);
                userInput = scanner.nextDouble();
                isValid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
            scanner.nextLine();
        } while (!isValid);

        return userInput;
    }

    private void displayMainMenu() {
        int choice = getValidIntInput(MAIN_MENU);
        switch (choice) {
            case 1 -> {
                System.out.println("\nPantry Items:");
                displayItems(Category.PANTRY);
            }
            case 2 -> {
                System.out.println("\nMeat/Poultry/Seafood Items:");
                displayItems(Category.MPS);
            }
            case 3 -> {
                System.out.println("\nSnack Items:");
                displayItems(Category.SNACKS);
            }
            case -1 -> checkout();
            case -2 -> {
                System.out.println("Unsupported option: Program Exiting now...");
                System.exit(0);
            }
            default -> {
                System.out.println("Invalid input. Try again.");
                displayMainMenu();
            }
        }
    }

    private void displayItems(Category category) {
        AtomicInteger id = new AtomicInteger();
        itemsInCategory =  allItems.stream()
                .filter(item -> item.getCategory() == category)
                .collect(Collectors.toList());

        itemsInCategory.stream()
                .forEach(item -> {
                    System.out.println(ITEM_FORMAT.formatted(
                            id.getAndIncrement(),
                            item.getName(),
                            priceFrmtter.format(item.getPrice()),
                            item.getUnit()));
                });

        displayCart();
    }
    
    private void displayCart() {
        int choice = getValidIntInput(ITEM_CART);
        switch (choice) {
            case 0, 1, 2, 3, 4 -> addToCart(itemsInCategory.get(choice));
            case -1 -> displayMainMenu();
            default -> {
                System.out.println("Invalid input. Try again.");
                displayMainMenu();
            }
        }
    }
    
    private void addToCart(Item item) {
        double totalAmount;
        double quantity;

        if (!item.getUnit().equals("kg")) {
            quantity = getValidIntInput(ITEM_QTY);
            totalAmount = quantity * item.getPrice();
            for (int i=0; i<quantity; i++) {
                itemsInCart.add(item);
            }
        } else {
            quantity = getValidDoubleInput(ITEM_QTY_KG);
            totalAmount = quantity * item.getPrice();
            item.setTotalItemsInCart(quantity);
            item.setTotalAmount(totalAmount);
            itemsInCart.add(item);
        }

        System.out.print("\nItem added: ");
        System.out.println(CART_ITEM_FORMAT.formatted(
                item.getName(),
                priceFrmtter.format(item.getPrice()),
                item.getUnit(),
                quantity,
                totalAmount
        ));

        displayCurrentCart(false);
    }
    
    private void displayCurrentCart(boolean isCheckingOut) {
    	NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
		fmt.setMinimumFractionDigits(3);
        
        String summary = """
				Total amount: %s,
				Total amount compact: %s,
				Number of Items: %d
				""";

        System.out.println("Current cart contents:");
        if (!isCheckingOut) {
        	Map<String, Object> content = itemsInCart.stream().collect(Collectors.teeing(
                    Collectors.summingDouble(n -> n.getUnit().equals("kg") ? n.getTotalAmount() : n.getPrice()),
                    Collectors.counting(),
                    (sum, count) ->  Map.ofEntries(
							Map.entry("totalAmount", sum),
							Map.entry("countAmount", count)
							)));
        	
        	
        	String cartContent = summary.formatted(content.get("totalAmount"), fmt.format(content.get("totalAmount")), content.get("countAmount")).trim();
            System.out.println(cartContent);
        }

        Map<Integer, Item> uniqueItems = new HashMap<>();
        for (Item i : itemsInCart) {
            if (uniqueItems.get(i.getId()) == null) {
                i.setTotalItemsInCart(i.getUnit().equals("kg") ? i.getTotalItemsInCart() : 1);
                i.setTotalAmount(i.getTotalItemsInCart() * i.getPrice());
            } else {
                Item addedItem = uniqueItems.get(i.getId());
                if (addedItem.getUnit().equals("kg")) {
                    addedItem.setTotalItemsInCart(addedItem.getTotalItemsInCart() + i.getTotalItemsInCart());
                } else {
                    addedItem.setTotalItemsInCart(addedItem.getTotalItemsInCart() + 1);
                }

                addedItem.setTotalAmount(addedItem.getTotalItemsInCart() * addedItem.getPrice());
            }

            uniqueItems.put(i.getId(), i);

        }

        uniqueItems.values().forEach((var i) -> {
            System.out.println(CART_ITEM_FORMAT.formatted(i.getName(), i.getPrice(), i.getUnit(), i.getTotalItemsInCart(), i.getTotalAmount()));
        });

        if (!isCheckingOut) {
            displayCart();
        }
    }

    private void checkout() {
        if (itemsInCart.isEmpty()) {
            System.out.println("Cart is empty, nothing to checkout.");
        } else {
            displayCurrentCart(true);
            //
            itemsInCart = new ArrayList<>();
        }

        //displayMainMenu();
    }

    public static void main(String[] args) {

        List<Item> items = new ArrayList<>();
        try {
            Path filepath = Paths.get(FILE_DIR + FILENAME);
            String content = Files.readString(filepath);

            AtomicInteger id = new AtomicInteger();
            content.lines().filter(Predicate.not(String::isBlank)).forEach((var line) -> {
                String[] details = line.split(",");
                Item item = new Item(id.getAndIncrement(), details[0], Double.valueOf(details[1]), details[2], Category.getByDescription(details[3])); //validation?
                items.add(item);
            });

            GroceryApp app = new GroceryApp(items);
            app.displayMainMenu();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
