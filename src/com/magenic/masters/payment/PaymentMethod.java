package com.magenic.masters.payment;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//public abstract class PaymentMethod {
//
//    public abstract String getFileName();
//    protected abstract String getAccountDetails();
//
//    public String getPaymentDetails(double totalAmount, int totalItemsInCart) {
//
//        NumberFormat formatter = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
//        formatter.setMinimumFractionDigits(3);
//
//        String paymentDetails = """
//                %s
//                Total amount due:
//                Total amount: %f
//                Total amount compact: %s
//                Number of items: %d                 
//                """;
//
//        return paymentDetails.formatted(
//                getAccountDetails().stripTrailing(),
//                totalAmount, formatter.format(totalAmount), totalItemsInCart);
//    }
//
//}

public sealed interface PaymentMethod permits Bank, CreditCard, Gcash {

	public String getAccountDetails();
	public String getFileName();

	public default String getPaymentDetails(double totalAmount, int totalItemsInCart) {

		NumberFormat formatter = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
		formatter.setMinimumFractionDigits(3);

		String paymentDetails = """
				%s
				Total amount due:
				Total amount: %f
				Total amount compact: %s
				Number of items: %d                 
				""";

		return paymentDetails.formatted(
				getAccountDetails().stripTrailing(),
				totalAmount, formatter.format(totalAmount), totalItemsInCart);
	}
}

