package com.magenic.masters.payment;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public sealed interface PaymentMethod permits Bank, CreditCard, Gcash, COD {

	String getAccountDetails();
	String getFileName();

	default String getPaymentDetails(double totalAmount, int totalItemsInCart) {

		NumberFormat priceFrmtter = new DecimalFormat("#0.00");
		NumberFormat formatter = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
		formatter.setMinimumFractionDigits(3);

		String paymentDetails = """
				%s				
				Total amount: %s
				Total amount compact: %s
				Number of items: %d                 
				""";

		return paymentDetails.formatted(
				getAccountDetails().stripIndent(),
				priceFrmtter.format(totalAmount), formatter.format(totalAmount), totalItemsInCart);
	}
}

