package com.magenic.masters.payment;

public final class CreditCard implements PaymentMethod {

    @Override
    public String getFileName() {
        return "receipt_cc.txt";
    }

    @Override
	public String getAccountDetails() {
        return """
            Name on card: David Beckham
            Credit card number: 4028123456789012
            Expiry date: 12/2022
            """;
    }
}
