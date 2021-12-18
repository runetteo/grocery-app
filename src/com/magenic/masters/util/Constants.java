package com.magenic.masters.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public final class Constants {

    public static final NumberFormat COMPACT_PRICE_FORMATTER;
    public static final NumberFormat PRICE_FORMATTER = new DecimalFormat("#0.00");

    static {
        COMPACT_PRICE_FORMATTER = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        COMPACT_PRICE_FORMATTER.setMinimumFractionDigits(3);
    }

}
