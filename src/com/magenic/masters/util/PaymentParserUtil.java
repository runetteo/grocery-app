package com.magenic.masters.util;

import com.magenic.masters.payment.*;

import java.util.Scanner;

public final class PaymentParserUtil {
    public static Scanner scanner = new Scanner(System.in);

    public static Gcash parseNewGcashAccount() {
        String subscriber = "";
        String mobileNumber = "";

        System.out.print("Subscriber name: \n");
        subscriber = scanner.nextLine();

        System.out.print("Mobile number: \n");
        mobileNumber = scanner.nextLine();


        return new Gcash(subscriber, mobileNumber, null);
    }

    public static  CreditCard parseNewCreditCard() {
        String nameOnCard = "";
        String ccNumber = "";
        String expiryDate = "";

        System.out.print("Name on card: \n");
        nameOnCard = scanner.nextLine();

        System.out.print("Credit card number: \n");
        ccNumber = scanner.nextLine();

        System.out.print("Expiry date: \n");
        expiryDate = scanner.nextLine();

        return new CreditCard(nameOnCard, ccNumber, expiryDate);
    }

    public static  PaymentMethod parseNewBankAccount(int option) {
        String accountName = "";
        String accountNumber = "";
        String bankName = "";

        System.out.print("Account Name: \n");
        accountName = scanner.nextLine();

        System.out.print("Account Number: \n");
        accountNumber = scanner.nextLine();

        System.out.print("Bank Name: \n");
        bankName = scanner.nextLine();


        if(option == 1) return new SavingsAccount(accountName, accountNumber, "NA", bankName);

        else return new CheckingAccount(accountName, accountNumber, "NA", bankName);
    }

}
