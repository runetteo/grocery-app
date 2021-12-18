package com.magenic.masters.util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ScannerUtil {

    private Scanner scanner;

    public ScannerUtil() {
        this.scanner = new Scanner(System.in);
    }

    public String getValidStringInput(String message) {
        String input = "";
        boolean isInvalid = true;
        do {
            System.out.print(message);
            input = scanner.next();
            isInvalid = input.strip().isBlank();

            if (isInvalid) {
                System.out.println("Invalid input. Account info cannot have blank values.");
            }
        } while (isInvalid);
        return input;
    }

    public int getValidIntInput(String message) {
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

    public double getValidDoubleInput(String message) {
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

}
