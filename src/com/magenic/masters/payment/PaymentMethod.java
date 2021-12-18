package com.magenic.masters.payment;

import com.magenic.masters.util.Constants;

public sealed interface PaymentMethod permits Bank, CreditCard, Gcash, COD {

	String getAccountDetails();
	String getFileName();

	default String getPaymentDetails(double totalAmount, int totalItemsInCart) {
		String paymentDetails = """
				%s				
				Total amount: %s
				Total amount compact: %s
				Number of items: %d                 
				""";

		return paymentDetails.formatted(
				getAccountDetails().stripIndent(),
				Constants.PRICE_FORMATTER.format(totalAmount), Constants.COMPACT_PRICE_FORMATTER.format(totalAmount), totalItemsInCart);
	}
}

