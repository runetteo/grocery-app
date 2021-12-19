package com.magenic.masters.payment;

public final class CheckingAccount extends Bank {
    public CheckingAccount(String accountName, String accountNumber) {
        super(accountName, accountNumber, "NA");
    }

    public CheckingAccount(String accountName, String accountNumber, String accountNickname) {
        super(accountName, accountNumber, accountNickname);
    }

    public CheckingAccount(String accountName, String accountNumber, String accountNickname, String bankName) {
        super(accountName, accountNumber, accountNickname, bankName);
    }

}
