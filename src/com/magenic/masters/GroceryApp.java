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
import com.magenic.masters.util.PaymentParserUtil;

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
               \nPayment Method: 
                1 - Savings
                2 - Checking
                3 - Credit Card
                4 - GCASH 
                
                Choose Payment Method:""";

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

    private int totalItemsInCart;
    private double totalAmount;

    public GroceryApp(List<Item> items, List<PaymentMethod> existingPaymentMethods) {
        this.allItems = items;
        this.existingPaymentMethods = existingPaymentMethods;
        this.scanner = new Scanner(System.in);
        this.itemsInCart = new ArrayList<>();
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


    private static String getPaymentMethodString(List<PaymentMethod> paymentMethods) {
        List<String> paymentMethodString = new ArrayList<>();
        paymentMethods.forEach(paymentMethod -> {
            if (paymentMethod instanceof SavingsAccount account) {
                paymentMethodString.add("[0]" + account.getAccountDetails());
            } else if (paymentMethod instanceof CheckingAccount account) {
                paymentMethodString.add("[1]" + account.getAccountDetails());
            } else if (paymentMethod instanceof CreditCard account) {
                paymentMethodString.add("[2]" + account.getAccountDetails());
            } else if (paymentMethod instanceof Gcash account) {
                paymentMethodString.add("[3]" + account.getAccountDetails());
            }
        });

        paymentMethodString.add("[4] COD \n");
        paymentMethodString.add("[5] Pay with other account \n");

        return paymentMethodString.stream().collect(Collectors.joining("\n"));
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
                            Constants.PRICE_FORMATTER.format(item.getPrice()),
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
        double quantity = getValidDoubleInput(item.getUnit().equals("kg") ? ITEM_QTY_KG : ITEM_QTY);

        if (quantity > 0) {
            if (!item.getUnit().equals("kg")) {
                totalAmount = quantity * item.getPrice();
                for (int i=0; i<quantity; i++) {
                    itemsInCart.add(item);
                }
            } else {
                totalAmount = quantity * item.getPrice();
                item.setTotalItemsInCart(quantity);
                item.setTotalAmount(totalAmount);
                itemsInCart.add(item);
            }

            System.out.print("\nItem added: ");
            System.out.println(CART_ITEM_FORMAT.formatted(
                    item.getName(),
                    Constants.PRICE_FORMATTER.format(item.getPrice()),
                    item.getUnit(),
                    quantity,
                    Constants.PRICE_FORMATTER.format(totalAmount)
            ));
        } else {
            System.out.println("Quantity selected was 0. Nothing was added.");
        }

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
                    Constants.PRICE_FORMATTER.format(content.get("totalAmount")),
                    Constants.COMPACT_PRICE_FORMATTER.format(content.get("totalAmount")),
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
            System.out.println(CART_ITEM_FORMAT.formatted(i.getName(), Constants.PRICE_FORMATTER.format(i.getPrice()),
                    i.getUnit(), i.getTotalItemsInCart(), Constants.PRICE_FORMATTER.format(i.getTotalAmount())));
        });

        if (!isCheckingOut) {
            displayOptionsForItems();
        }
    }

    private void displayAndSaveReceipt() {
        String message = """
                
                Payment Methods:
                
                %s
                
                Choose Payment Method:""";
       int choice = getValidIntInput(message.formatted(getPaymentMethodString(existingPaymentMethods)));
        switch (choice) {
            case 0, 1, 2, 3 -> saveReceipt(existingPaymentMethods.get(choice));
            case 4 -> saveReceipt(new COD());
            case 5 -> saveReceipt(createNewPayment());
            default ->  displayAndSaveReceipt();
        };
    }

    private PaymentMethod createNewPayment() {
        int choice = getValidIntInput(PAYMENT_OPTIONS_MENU);
        System.out.print("Enter Account Info \n");
        return switch (choice) {
             case 1, 2 : yield PaymentParserUtil.parseNewBankAccount(choice);
             case 3 : yield PaymentParserUtil.parseNewCreditCard();
             case 4 : yield PaymentParserUtil.parseNewGcashAccount();
             default :  yield createNewPayment();
        };

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
            return switch (details[0]) {
                case "Savings": yield new SavingsAccount(details[1], details[2], details[3]);
                case "Checking": yield new CheckingAccount(details[1], details[2], details[3]);
                case "Credit card": yield new CreditCard(details[1], details[2], details[3], details[4]);
                case "Gcash": yield new Gcash(details[1], details[2], details[3]);
                default: yield  null;
            };

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
