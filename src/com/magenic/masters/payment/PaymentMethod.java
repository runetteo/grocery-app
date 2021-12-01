package com.magenic.masters.payment;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class PaymentMethod {

    protected abstract String getAccountDetails();

    public String getPaymentDetails() {

        NumberFormat formatter = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        formatter.setMinimumFractionDigits(3);

        String amountFormat = """
                "totalPayableAsString":
                 {
                    "totalAmount": %f,
                    "totalAmount compact: %s,
                    "number of items": %d
                 }                  
                """;
        String totalPayable = Stream.of(100, 1000, 100000, 1000000).collect(Collectors.teeing(
                Collectors.summingDouble(i -> i),
                Collectors.counting(),
                (sum, count) -> amountFormat.formatted(sum, formatter.format(sum), count)));

        String result = """
                {
                 %s
                 %s
                }
                """;

        return result.formatted(
                getAccountDetails().stripTrailing(),
                totalPayable.stripTrailing());
    }

}
