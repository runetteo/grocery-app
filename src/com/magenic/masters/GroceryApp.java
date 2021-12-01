package com.magenic.masters;


import com.magenic.masters.item.Item;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class GroceryApp {

    public static void main(String[] args) {

        List<Item> items = new ArrayList<>();

        try {
            Path filepath = Paths.get("C:/Users/RunetteO/workspace/stocks.csv");
            String content = null;
            content = Files.readString(filepath);

            content.lines().filter(Predicate.not(String::isBlank)).forEach((var line) ->{
                String[] details = line.split(",");
                

            });


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
