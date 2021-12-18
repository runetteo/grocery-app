package com.magenic.masters.payment;

public sealed class Bank implements PaymentMethod permits CheckingAccount, SavingsAccount {
    
    private String accountName;
    private String accountNumber;
    private String accountNickname;
    private String bankName;

    public Bank(String accountName, String accountNumber, String accountNickname) {
        this(accountName, accountNumber, accountNickname, "BDO");
    }

    public Bank(String accountName, String accountNumber, String accountNickname, String bankName) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.accountNickname = accountNickname;
        this.bankName = bankName;
    }

    @Override
    public String getAccountDetails() {
        String details = """
                Account nickname:\s%s
                Account name:\s%s
                Account number:\s%s
                Account type:\s%s
                Bank name:\s%s
                """;

        String accountType = this instanceof SavingsAccount ? "Savings" : "Checking";
        return details.formatted(this.accountNickname, this.accountName, this.accountNumber, accountType, this.bankName);
    }
    
    @Override
    public String getFileName() {
        return "receipt_bank.txt";
    }
}
