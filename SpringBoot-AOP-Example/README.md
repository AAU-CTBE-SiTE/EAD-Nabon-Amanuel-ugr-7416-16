# SpringBoot-AOP-Example

A simple beginner-level Spring Boot project demonstrating Aspect-Oriented Programming (AOP) logging with `@Aspect`, `@Before`, and `@After`.

## Technologies Used

- **Java 17**
- **Spring Boot 3.5.3** (`spring-boot-starter`, `spring-boot-starter-aop`)
- **Maven**

## Project Structure

```text
SpringBoot-AOP-Example/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/com/craft/aop/
        │   ├── SpringBootAopExampleApplication.java
        │   ├── aspect/
        │   │   └── LoggingAspect.java
        │   └── service/
        │       └── BankService.java
        └── resources/
            └── application.properties
```

## How It Works

- **`BankService`** contains simple business methods (`deposit`, `withdraw`, `checkBalance`) without any logging code inside them.
- **`LoggingAspect`** uses `@Aspect` and `@Component` to separate logging from the service logic:
  - `@Before("execution(* com.craft.aop.service.BankService.*(..))")` prints a message before each `BankService` method runs.
  - `@After("execution(* com.craft.aop.service.BankService.*(..))")` prints a message after each `BankService` method finishes.
- **`SpringBootAopExampleApplication`** fetches `BankService` from the Spring context and calls `deposit(500)`, `withdraw(200)`, and `checkBalance()`.

## How to Run

1. Navigate to the project folder:
   ```bash
   cd SpringBoot-AOP-Example
   ```
2. Run with Maven:
   ```bash
   mvn spring-boot:run
   ```

## Example Console Output

```text
[LOG - Before] Calling method: deposit
Deposited $500.0. New balance: $1500.0
[LOG - After] Finished method: deposit
[LOG - Before] Calling method: withdraw
Withdrew $200.0. Remaining balance: $1300.0
[LOG - After] Finished method: withdraw
[LOG - Before] Calling method: checkBalance
Current balance: $1300.0
[LOG - After] Finished method: checkBalance
```
