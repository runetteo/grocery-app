package com.magenic.masters.payment;

public class Gcash extends PaymentMethod {

    @Override
    public String getFileName() {
        return "receipt_gcash.txt";
    }

    @Override
    protected String getAccountDetails() {
        return """
            Subscriber name: David Beckham
            Mobile number: 09171234567
            """;
    }
}
