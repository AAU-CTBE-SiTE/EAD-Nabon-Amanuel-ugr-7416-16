package com.craft.aop;

import com.craft.aop.service.BankService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringBootAopExampleApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringBootAopExampleApplication.class, args);

        BankService bankService = context.getBean(BankService.class);

        bankService.deposit(500);
        bankService.withdraw(200);
        bankService.checkBalance();
    }
}
