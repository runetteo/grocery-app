package com.magenic.masters.payment;

public class Bank extends PaymentMethod{

    @Override
    public String getFileName() {
        return "receipt_bank.txt";
    }

    @Override
    protected String getAccountDetails() {
        return """
            Account name: David Beckham
            Account number: 005412345678
            Bank name: BDO
            """;
    }
}
