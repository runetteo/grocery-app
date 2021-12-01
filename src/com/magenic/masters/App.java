package com.magenic.masters;

import com.magenic.masters.payment.BDO;
import com.magenic.masters.payment.CreditCard;
import com.magenic.masters.payment.Gcash;
import com.magenic.masters.payment.PaymentMethod;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("""
                Choose payment method: 
                1 - Savings and Checking
                2 - Credit Card
                3 - GCASH 
                """);

        int choice = scanner.nextInt();
        PaymentMethod paymentMethod = switch (choice) {
            case 1 -> new BDO();
            case 2 -> new CreditCard();
            case 3 -> new Gcash();
            default -> null;
        };

        System.out.println("Payment Details = ");
        System.out.println(paymentMethod != null ? paymentMethod.getPaymentDetails() : "Unknown");
    }
}
