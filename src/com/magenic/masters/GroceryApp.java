package com.magenic.masters;

import com.magenic.masters.item.Category;
import com.magenic.masters.item.Item;
import com.magenic.masters.payment.COD;
import com.magenic.masters.payment.CheckingAccount;
import com.magenic.masters.payment.CreditCard;
import com.magenic.masters.payment.Gcash;
import com.magenic.masters.payment.PaymentMethod;
import com.magenic.masters.payment.SavingsAccount;
import com.magenic.masters.util.Constants;
import com.magenic.masters.util.FileUtil;
import com.magenic.masters.util.Parser;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
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

    private static final String PAYMENT_OPTIONS_MENU = """
                Choose payment method: 
                1 - Savings
                2 - Checking
                3 - Credit Card
                4 - GCASH 
                """;

    private static final String ITEM_CART = """
            \nChoose item (-1 to go back to Categories):""";

    private static final String ITEM_QTY = """
            Enter how many:""";

    private static final String ITEM_QTY_KG = "Enter how much(in kg):";

    private static final String ITEM_FORMAT = "[%d]%-55s price: %s/%s";
    private static final String CART_ITEM_FORMAT = "%s | %s/%s x %s | %s";
    private static final String STOCKS_FILENAME = "stocks.csv";
    private static final String ACCOUNTS_FILENAME = "accounts.csv";

    private Scanner scanner;
    private List<Item> itemsInCart;
    private List<Item> allItems;
    private List<Item> itemsInCategory;
    private List<PaymentMethod> existingPaymentMethods;
    private NumberFormat priceFrmtter;

    private int totalItemsInCart;
    private double totalAmount;

    public GroceryApp(List<Item> items, List<PaymentMethod> existingPaymentMethods) {
        this.allItems = items;
        this.existingPaymentMethods = existingPaymentMethods;
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

        displayOptionsForItems();
    }

    private void displayOptionsForItems() {
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
                priceFrmtter.format(totalAmount)
        ));

        displayCurrentCart(false);
    }

    private void displayCurrentCart(boolean isCheckingOut) {

        String summary = """
				Total amount: %s
				Total amount compact: %s
				Number of Items: %d				
				""";

        System.out.println("\nCurrent cart contents:");
        if (!isCheckingOut) {
            Map<String, Object> content = itemsInCart.stream().collect(Collectors.teeing(
                    Collectors.summingDouble(n -> n.getUnit().equals("kg") ? n.getTotalAmount() : n.getPrice()),
                    Collectors.counting(),
                    (sum, count) ->  Map.ofEntries(
                            Map.entry("totalAmount", sum),
                            Map.entry("countAmount", count)
                    )));

            totalItemsInCart = Integer.valueOf(String.valueOf(content.get("countAmount")));
            totalAmount = Double.valueOf(String.valueOf(content.get("totalAmount")));

            String cartContent = summary.formatted(
                    Constants.priceFrmtter.format(content.get("totalAmount")),
                    Constants.compactNumFmt.format(content.get("totalAmount")),
                    content.get("countAmount"));
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
            System.out.println(CART_ITEM_FORMAT.formatted(i.getName(), priceFrmtter.format(i.getPrice()),
                    i.getUnit(), i.getTotalItemsInCart(), priceFrmtter.format(i.getTotalAmount())));
        });

        if (!isCheckingOut) {
            displayOptionsForItems();
        }
    }

    private void displayAndSaveReceipt() {
        String message = """
                
                Payment Methods:
                
                Choose Payment Method:""";
        //TODO: add existing methods.
        int choice = getValidIntInput(message);
        switch (choice) {
            case 0, 1, 2, 3 -> saveReceipt(existingPaymentMethods.get(choice));
            case 4 -> saveReceipt(new COD());
            case 5 -> createNewPayment();
            default ->  displayAndSaveReceipt();
        };
    }

    private void createNewPayment() {
//        int choice = getValidIntInput(PAYMENT_OPTIONS_MENU);
    }

    private void saveReceipt(PaymentMethod paymentMethod) {
        String paymentDetails;

        if (paymentMethod instanceof COD c && c.getAccountDetails() != null) {
            System.out.println("""
                
                Thank you for shopping.
                You have selected to pay by COD.
                Please prepare amount below:""");
        } else {
            System.out.println("""
                
                Thank you for your payment.
                Payment Details:""");
        }

        switch (paymentMethod) {
            case SavingsAccount s -> paymentDetails = s.getPaymentDetails(totalAmount, totalItemsInCart);
            case CheckingAccount c -> paymentDetails = c.getPaymentDetails(totalAmount, totalItemsInCart);
            case CreditCard cc -> paymentDetails = cc.getPaymentDetails(totalAmount, totalItemsInCart);
            case Gcash g -> paymentDetails = g.getPaymentDetails(totalAmount, totalItemsInCart);
            case COD cod -> paymentDetails = cod.getPaymentDetails(totalAmount, totalItemsInCart);
            default -> paymentDetails = paymentMethod.getPaymentDetails(totalAmount, totalItemsInCart);
        }

        System.out.println(paymentDetails);
        FileUtil.saveToFile(paymentDetails, paymentMethod.getFileName());
    }

    private void checkout() {
        if (itemsInCart.isEmpty()) {
            System.out.println("Cart is empty, nothing to checkout.");
        } else {
            displayCurrentCart(true);
            displayAndSaveReceipt();
            itemsInCart = new ArrayList<>();
        }

        displayMainMenu();
    }

    public static void main(String[] args) {

        Parser<String, Item> itemParser = (String s) -> {
            String[] details = s.split(",");
            return new Item(details[0], Double.valueOf(details[1]),
                    details[2], Category.getByDescription(details[3]));
        };

        Parser<String, PaymentMethod> accountParser = (String s) -> {
            String[] details = s.split(",");
            PaymentMethod p = switch (details[0]) {
                case "Savings": yield new SavingsAccount(details[1], details[2], details[3]);
                case "Checking": yield new CheckingAccount(details[1], details[2], details[3]);
                case "Credit card": yield new CreditCard();
                case "Gcash": yield new Gcash(details[1], details[2], details[3]);
                default: yield  null;
            };

            if (p instanceof CreditCard c) {
                c.setAccountName(details[1]);
                c.setAccountNumber(details[2]);
                c.setAccountNickname(details[3]);
                c.setExpiryDate(details[4]);
            }
            return p;
        };


        List<Item> items = new ArrayList<>();
        String stocks = FileUtil.readFile(STOCKS_FILENAME);
        String accounts = FileUtil.readFile(ACCOUNTS_FILENAME);

        AtomicInteger id = new AtomicInteger();
        stocks.lines().filter(Predicate.not(String::isBlank)).forEach((var line) -> {
            Item item = itemParser.parse(line);
            item.setId(id.getAndIncrement());
            items.add(item);
        });

        List<PaymentMethod> existingPaymentMethods = new ArrayList<>();
        accounts.lines().filter(Predicate.not(String::isBlank)).forEach((var line) -> {
            existingPaymentMethods.add(accountParser.parse(line));
        });

        GroceryApp app = new GroceryApp(items, existingPaymentMethods);
        app.displayMainMenu();

    }

}
