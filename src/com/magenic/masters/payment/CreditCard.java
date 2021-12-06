package com.magenic.masters.payment;

public class CreditCard extends PaymentMethod {

    @Override
    public String getFileName() {
        return "receipt_cc.txt";
    }

    @Override
    protected String getAccountDetails() {
        return """
            Name on card: David Beckham
            Credit card number: 4028123456789012
            Expiry date: 12/2022
            """;
    }
}
