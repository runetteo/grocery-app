package com.magenic.masters;

import com.magenic.masters.item.Category;
import com.magenic.masters.item.Item;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class GroceryApp {

    public static void main(String[] args) {

        List<Item> items = new ArrayList<>();
        try {
            Path filepath = Paths.get("C:/Users/RunetteO/workspace/stocks.csv");
            String content = Files.readString(filepath);

            content.lines().filter(Predicate.not(String::isBlank)).forEach((var line) ->{
                String[] details = line.split(",");
                Item item = new Item(details[0], Double.valueOf(details[1]), details[2], Category.getByDescription(details[3])); //validation?
                items.add(item);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        String menu1 = """
                Category:
                    1 - Pantry supplies
                    2 - Meat/Poultry/Seafood
                    3 - Snacks
                    
                Choose Cateogry(-1 to Checkout, -2 to Exit):
                """;

        Scanner scanner = new Scanner(System.in);
        System.out.print(menu1);

        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> System.out.printf("gdsfgsd");
            case 2 -> System.out.printf("gdsfgsd");
            case 3 -> System.out.printf("gdsfgsd");
            default -> System.out.println("exit");
        }

        System.out.println("ITEMS:");
        items.forEach(System.out::println);

        //tuloy ko mamaya after shift hehe

    }

}
