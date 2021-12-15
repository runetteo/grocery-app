package com.magenic.masters.payment;

public sealed class Bank implements PaymentMethod permits CheckingAccount, SavingsAccount {

//    @Override
//    protected String getAccountDetails() {
//        return """
//            Account name: David Beckham
//            Account number: 005412345678
//            Bank name: BDO
//            """;
//    }
    
    private String accountName;
    private String accountNumber;

    public Bank(String accountName, String accountNumber) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
    }

    @Override
    public String getAccountDetails() {
        String details = """
				Account Name:\s%s
				Account Number:\s%s
				Bank name:\sBDO""";
        return details.formatted(this.accountName, this.accountNumber);
    }
    
    @Override
    public String getFileName() {
        return "receipt_bank.txt";
    }
}
