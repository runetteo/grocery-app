package com.magenic.masters.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public final class Constants {

    public static final NumberFormat compactNumFmt;
    public static final NumberFormat priceFrmtter = new DecimalFormat("#0.00");

    static {
        compactNumFmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        compactNumFmt.setMinimumFractionDigits(3);
    }

}
