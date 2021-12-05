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
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    		\nEnter how many:""";
    
    private static final String ITEM_FORMAT = "[%d]%-55s price: %s/%s";
    private static final String CART_ITEM_FORMAT = "\nItem added: %s | %s/%s x %s | %s \nCurrent cart contents:\n";
    private static final String FILE_DIR = "resources/";
    private static final String FILENAME = "stocks.csv";
    private int i = 0;

    private Scanner scanner;
    private List<Item> itemsInCart;
    private List<Item> items;
    private NumberFormat priceFrmtter;

    public GroceryApp(List<Item> items) {
        this.items = items;
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

    private void displayMainMenu() {
        int choice = getValidIntInput(MAIN_MENU);
        switch (choice) {
            case 1 -> {
                System.out.println("\nPantry Items:");
                displayItems(Category.PANTRY);
                displayCart();
            }
            case 2 -> {
                System.out.println("\nMeat/Poultry/Seafood Items:");
                displayItems(Category.MPS);
                displayCart();
            }
            case 3 -> {
                System.out.println("\nSnack Items:");
                displayItems(Category.SNACKS);
                displayCart();
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
                    System.out.println(ITEM_FORMAT.formatted(
                            id.getAndIncrement(),
                            item.getName(),
                            priceFrmtter.format(item.getPrice()),
                            item.getUnit()));
                    item.setTotalAmount(0);
                    item.setTotalItemsInCart(0);
                });
    }
    
    private void displayCart() {
        int choice = getValidIntInput(ITEM_CART);
        switch (choice) {
            case 0 -> {
            	addToCart(choice);
            }
            case 1 -> {
            	addToCart(choice);
            }
            case 2 -> {
            	addToCart(choice);
            }
            case 3 -> {
            	addToCart(choice);
            }
            case 4 -> {
            	addToCart(choice);
            }
            case -1 -> {
            	i = 0;
            	displayMainMenu();
            }
            default -> {
                System.out.println("Invalid input. Try again.");
                displayMainMenu();
            }
        }
    }
    
    private void addToCart(int item) {
    	int quantity = getValidIntInput(ITEM_QTY);
    	itemsInCart.add(items.get(item));

    	if (itemsInCart.size() != 0) {
    		getCart(quantity, item);
    	}
    }
    
    private void getCart(int quantity, int item) {
    	double price = items.get(item).getPrice();
    	double totalAmount = price*quantity;

    	System.out.println(CART_ITEM_FORMAT.formatted(
    			items.get(item).getName(),
    			priceFrmtter.format(price),
    			items.get(item).getUnit(),
    			quantity,
    			items.get(item).setTotalAmount(totalAmount)
    			));
    	
    	displayCurrentCart(item);
    }
    
    private void displayCurrentCart(int item) {
    	Map<String, Object> currentCart = Stream.of(itemsInCart).collect(
				Collectors.teeing(
						Collectors.summingDouble(n -> items.get(item).getTotalAmount()),
						Collectors.counting(),
						(sum, count) ->  Map.ofEntries(
								Map.entry("totalAmount", sum),
								Map.entry("countAmount", count)
								)
						));
    	
    	NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
		fmt.setMinimumFractionDigits(3);

		String contents = """
				Total amount: %s,
				Total amount compact: %s,
				Number of Items: %s
				""";

		String cartContent = contents.formatted(currentCart.get("totalAmount"), fmt.format(currentCart.get("totalAmount")), currentCart.get("countAmount")).trim();
		System.out.println(cartContent);
		displayCart();
    }
    

    public static void main(String[] args) {

        List<Item> items = new ArrayList<>();
        try {
            Path filepath = Paths.get(FILE_DIR + FILENAME);
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
