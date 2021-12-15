package com.magenic.masters.payment;

public record Gcash(String subscriberName, String mobileNumber) implements PaymentMethod {

    @Override
    public String getFileName() {
        return "receipt_gcash.txt";
    }

    @Override
    public String getAccountDetails() {
    	String accountDetails = """
				Subscriber name:\s%s
				Mobile Number:\s%s""";
        return accountDetails.formatted(subscriberName, mobileNumber);
    }
}
