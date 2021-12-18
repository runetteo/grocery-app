package com.magenic.masters.payment;

public record Gcash(String subscriberName, String mobileNumber, String accountNickname) implements PaymentMethod {

    @Override
    public String getFileName() {
        return "receipt_gcash.txt";
    }

    @Override
    public String getAccountDetails() {
    	String accountDetails = """
                Account nickname:\s%s
				Subscriber name:\s%s
				Mobile number:\s%s
				Account type:\sGcash
				""";
        return accountDetails.formatted(accountNickname == null ? "NA" : accountNickname, subscriberName, mobileNumber);
    }
}
