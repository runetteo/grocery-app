package com.magenic.masters.payment;

public final class CreditCard implements PaymentMethod {

	 private final String  accountName = "Mau Tuazon";
     private final String  accountNumber = "4028123456789012";

    @Override
    public String getFileName() {
        return "receipt_cc.txt";
    }

    @Override
    public String getAccountDetails() {
        String details = """
				Name on Card:\s%s
				Credit Card number:\s%s
				Expiry Date:\s12/2022""";
        return details.formatted(this.accountName, this.accountNumber);
    }
}
