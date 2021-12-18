package com.magenic.masters.payment;

public final class COD implements PaymentMethod {
    @Override
    public String getAccountDetails() {
        return "COD";
    }

    @Override
    public String getFileName() {
        return "receipt_cod.txt";
    }
}
