package com.magenic.masters.util;

import com.magenic.masters.payment.*;

import java.util.Scanner;

public final class PaymentParserUtil {
    public static Scanner scanner = new Scanner(System.in);

    public static Gcash parseNewGcashAccount() {

        String subscriber = getValidStringInput("Subscriber name: ");
        String mobileNumber = getValidStringInput("Mobile number: ");


        return new Gcash(subscriber, mobileNumber, null);
    }

    public static  CreditCard parseNewCreditCard() {

        String nameOnCard = getValidStringInput("Name on card: ");
        String ccNumber = getValidStringInput("Credit card number: ");
        String expiryDate = getValidStringInput("Expiry date: ");

        return new CreditCard(nameOnCard, ccNumber, expiryDate);
    }

    public static  PaymentMethod parseNewBankAccount(int option) {

        String accountName = getValidStringInput("Account Name: ");
        String accountNumber = getValidStringInput("Account Number: ");
        String bankName = getValidStringInput("Bank Name: ");


        if(option == 1) return new SavingsAccount(accountName, accountNumber, "NA", bankName);

        else return new CheckingAccount(accountName, accountNumber, "NA", bankName);
    }

    private static String getValidStringInput(String message) {
        String input = "";
        boolean isInvalid = true;
        do {
            System.out.print(message);
            input = scanner.nextLine();
            isInvalid = input.strip().isBlank();

            if (isInvalid) {
                System.out.println("Invalid input. Account info cannot have blank values.");
            }
        } while (isInvalid);
        return input;
    }

}
