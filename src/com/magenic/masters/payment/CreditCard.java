package com.magenic.masters.payment;

public class CreditCard extends PaymentMethod {
    @Override
    protected String getAccountDetails() {
        return """
            "nameOnCard": "David Beckham",
             "creditCardNumber": "4028123456789012",
             "expiryDate": "12/2022",
            """;
    }
}
