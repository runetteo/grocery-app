package com.magenic.masters.payment;

public class Gcash extends PaymentMethod {

    @Override
    protected String getAccountDetails() {
        return """
            "subscriberName": "David Beckham",
             "mobileNumber": "09171234567",
            """;
    }
}
