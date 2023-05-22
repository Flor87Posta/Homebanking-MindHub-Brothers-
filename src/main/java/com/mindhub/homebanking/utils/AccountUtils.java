package com.mindhub.homebanking.utils;

public final class AccountUtils {

    public static String getAccountNumber() { //public para poder ser accedida desde el controller
        String accountNumber;
        int numberGenerated = (int) (Math.random() * 1000);
        accountNumber = "VIN" + String.format("%08d", numberGenerated);
        return accountNumber;
    }

}
