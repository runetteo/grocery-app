package com.magenic.masters.payment;

public final class CreditCard implements PaymentMethod {

    private String accountName;
    private String accountNumber;
    private String accountNickname;
    private String expiryDate;

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountNickname(String accountNickname) {
        this.accountNickname = accountNickname;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String getFileName() {
        return "receipt_cc.txt";
    }

    @Override
    public String getAccountDetails() {
        String details = """
                Account nickname:\s%s
                Name on card:\s%s
                Credit card number:\s%s
                Account type: Credit card
                Expiry Date:\s%s
                """;

        return details.formatted(this.accountNickname, this.accountName, this.accountNumber, this.expiryDate);
    }
}
