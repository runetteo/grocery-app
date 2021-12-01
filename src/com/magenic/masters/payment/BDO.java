package com.magenic.masters.payment;

public class BDO extends PaymentMethod{

    @Override
    protected String getAccountDetails() {
        return """
            "accountName": "David Beckham",
             "accountNumber": "005412345678",
             "bankName": "BDO",
            """;
    }
}
