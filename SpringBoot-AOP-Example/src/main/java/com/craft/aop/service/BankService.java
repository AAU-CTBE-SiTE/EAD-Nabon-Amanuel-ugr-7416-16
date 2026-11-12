package com.craft.aop.service;

import org.springframework.stereotype.Service;

@Service
public class BankService {

    private double balance = 1000.0;

    public void deposit(double amount) {
        balance = balance + amount;
        System.out.println("Deposited $" + amount + ". New balance: $" + balance);
    }

    public void withdraw(double amount) {
        if (amount > balance) {
            System.out.println("Insufficient balance to withdraw $" + amount);
            return;
        }
        balance = balance - amount;
        System.out.println("Withdrew $" + amount + ". Remaining balance: $" + balance);
    }

    public void checkBalance() {
        System.out.println("Current balance: $" + balance);
    }
}
