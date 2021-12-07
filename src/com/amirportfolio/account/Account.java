package com.amirportfolio.account;

import com.amirportfolio.database.DataManager;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Scanner;

public class Account {

    private String name;
    private int accountNumber;
    private String password;

    private Scanner scanner  = new Scanner(System.in);

    public Account() {

    }

    //-----------------------------------------------------------------------

    public void setupNewAccount(String name, int account, String password) {
        if (!name.isEmpty() && account!=0 && !password.isEmpty()) {
            setName(name);
            setAccountNumber(account);
            setPassword(encrypt(password));
        }
    }

    public void accessAccount(int accountNumber) {
        this.accountNumber = accountNumber;
        System.out.println("From accessAccount: " + getAccountNumber());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = encrypt(password);
    }

    public String receiveNewPassword() {

        String newPass = "a";
        String newPassRepeat = "b";

        while (!newPass.equals(newPassRepeat)) {
            System.out.println("Enter new password: ");
            newPass = scanner.nextLine();
            System.out.println("Enter new password one more time: ");
            newPassRepeat = scanner.nextLine();
        }

        return newPass;
    }

    /**
    * Return an encrypted string of the input string
     */
    public String encrypt(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return byteToString(hash);
        } catch (Exception e) {
            System.out.println("Failed to encrypt! " + e.getMessage());
            return null;
        }
    }

    /**
     * Return string value of the input byte array
     */
    private String byteToString(byte[] input) {
        BigInteger number = new BigInteger(1, input);
        StringBuilder sb = new StringBuilder(number.toString(16));

        while(sb.length() < 32) {
            sb.insert(0, '0');
        }

        return sb.toString();
    }

}
