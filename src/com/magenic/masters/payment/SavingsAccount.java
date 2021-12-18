package com.magenic.masters.payment;

public final class SavingsAccount extends Bank {
    public SavingsAccount(String accountName, String accountNumber) {
        super(accountName, accountNumber, "NA");
    }

    public SavingsAccount(String accountName, String accountNumber, String accountNickname) {
        super(accountName, accountNumber, accountNickname);
    }
}
