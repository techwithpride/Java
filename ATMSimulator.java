import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/* ---------------------------------------------------------
   Transaction: one record in the account history
   --------------------------------------------------------- */
class Transaction {
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final String type;
    private final double amount;
    private final double balanceAfter;
    private final String time;

    Transaction(String type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.time = LocalDateTime.now().format(FMT);
    }

    @Override
    public String toString() {
        return String.format("%-19s | %-14s | %10.2f | Bal: %10.2f",
                time, type, amount, balanceAfter);
    }
}

/* ---------------------------------------------------------
   Account: holds balance, hashed PIN, lock status, history
   --------------------------------------------------------- */
class Account {
    private static final int MAX_ATTEMPTS = 3;

    private final String cardNumber;
    private final String holderName;
    private String pinHash;
    private double balance;
    private int failedAttempts;
    private boolean locked;
    private final List<Transaction> history = new ArrayList<>();

    Account(String cardNumber, String holderName, String pin, double openingBalance) {
        this.cardNumber = cardNumber;
        this.holderName = holderName;
        this.pinHash = hashPin(pin);
        this.balance = openingBalance;
        if (openingBalance > 0) {
            history.add(new Transaction("OPENING DEPOSIT", openingBalance, balance));
        }
    }

    // PIN is never stored as plain text: SHA-256 with the card number as salt
    private String hashPin(String pin) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest((cardNumber + pin).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    boolean verifyPin(String pin) {
        if (locked) return false;
        if (pinHash.equals(hashPin(pin))) {
            failedAttempts = 0;
            return true;
        }
        failedAttempts++;
        if (failedAttempts >= MAX_ATTEMPTS) locked = true;
        return false;
    }

    int attemptsLeft() { return MAX_ATTEMPTS - failedAttempts; }
    boolean isLocked() { return locked; }
    String getCardNumber() { return cardNumber; }
    String getHolderName() { return holderName; }
    double getBalance() { return balance; }

    void deposit(double amount) {
        balance += amount;
        history.add(new Transaction("DEPOSIT", amount, balance));
    }

    boolean withdraw(double amount) {
        if (amount > balance) return false;
        balance -= amount;
        history.add(new Transaction("WITHDRAW", amount, balance));
        return true;
    }

    boolean changePin(String oldPin, String newPin) {
        if (!pinHash.equals(hashPin(oldPin))) return false;
        pinHash = hashPin(newPin);
        history.add(new Transaction("PIN CHANGED", 0, balance));
        return true;
    }

    List<Transaction> getHistory() { return history; }
}

/* ---------------------------------------------------------
   Bank: stores accounts and GENERATES new cards/accounts
   --------------------------------------------------------- */
class Bank {
    private final Map<String, Account> accounts = new HashMap<>();
    private final SecureRandom random = new SecureRandom();

    // Generates a unique 12-digit card number
    private String generateCardNumber() {
        String card;
        do {
            StringBuilder sb = new StringBuilder("4"); // starts with 4
            for (int i = 0; i < 11; i++) sb.append(random.nextInt(10));
            card = sb.toString();
        } while (accounts.containsKey(card));
        return card;
    }

    Account generateAccount(String name, String pin, double openingBalance) {
        String card = generateCardNumber();
        Account acc = new Account(card, name, pin, openingBalance);
        accounts.put(card, acc);
        return acc;
    }

    // Used only for the built-in demo account
    void addAccount(Account acc) {
        accounts.put(acc.getCardNumber(), acc);
    }

    Account find(String cardNumber) {
        return accounts.get(cardNumber);
    }
}

/* ---------------------------------------------------------
   ATM: the user interface (console menus)
   --------------------------------------------------------- */
class ATM {
    private static final double MAX_WITHDRAW = 20000;
    private static final double NOTE_MULTIPLE = 100;

    private final Bank bank;
    private final Scanner sc = new Scanner(System.in);

    ATM(Bank bank) { this.bank = bank; }

    /* ---------- input helpers ---------- */
    private String ask(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private double askAmount(String prompt) {
        while (true) {
            try {
                double v = Double.parseDouble(ask(prompt));
                if (v <= 0) {
                    System.out.println("Amount must be greater than 0.");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private boolean validPin(String pin) {
        return pin.matches("\\d{4}");
    }

    private String askNewPin(String prompt) {
        while (true) {
            String pin = ask(prompt);
            if (validPin(pin)) return pin;
            System.out.println("PIN must be exactly 4 digits.");
        }
    }

    /* ---------- main loop ---------- */
    void start() {
        while (true) {
            System.out.println("\n==============================");
            System.out.println("       WELCOME TO JAVA ATM    ");
            System.out.println("==============================");
            System.out.println("1. Generate new ATM card");
            System.out.println("2. Insert card (login)");
            System.out.println("3. Exit");
            String choice = ask("Choose: ");

            switch (choice) {
                case "1": generateCard(); break;
                case "2": login(); break;
                case "3":
                    System.out.println("Thank you for using Java ATM. Goodbye!");
                    return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    /* ---------- card generator ---------- */
    private void generateCard() {
        System.out.println("\n--- Generate New ATM Card ---");
        String name = ask("Account holder name: ");
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        String pin = askNewPin("Set a 4-digit PIN: ");
        double opening = askAmount("Opening deposit: ");

        Account acc = bank.generateAccount(name, pin, opening);
        System.out.println("\nCard generated successfully!");
        System.out.println("Holder      : " + acc.getHolderName());
        System.out.println("Card number : " + acc.getCardNumber());
        System.out.println("Balance     : " + acc.getBalance());
        System.out.println("Keep your card number safe. You need it to log in.");
    }

    /* ---------- login with PIN security ---------- */
    private void login() {
        System.out.println("\n--- Insert Card ---");
        String card = ask("Card number: ");
        Account acc = bank.find(card);
        if (acc == null) {
            System.out.println("Card not found.");
            return;
        }
        if (acc.isLocked()) {
            System.out.println("This card is LOCKED. Please contact the bank.");
            return;
        }
        while (!acc.isLocked()) {
            String pin = ask("Enter PIN: ");
            if (acc.verifyPin(pin)) {
                System.out.println("Login successful. Welcome, " + acc.getHolderName() + "!");
                session(acc);
                return;
            }
            if (acc.isLocked()) {
                System.out.println("Too many wrong attempts. Card is now LOCKED.");
            } else {
                System.out.println("Wrong PIN. Attempts left: " + acc.attemptsLeft());
            }
        }
    }

    /* ---------- logged-in menu ---------- */
    private void session(Account acc) {
        while (true) {
            System.out.println("\n--- ATM MENU ---");
            System.out.println("1. Check balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transaction history");
            System.out.println("5. Change PIN");
            System.out.println("6. Logout");
            String choice = ask("Choose: ");

            switch (choice) {
                case "1":
                    System.out.printf("Current balance: %.2f%n", acc.getBalance());
                    break;
                case "2":
                    acc.deposit(askAmount("Deposit amount: "));
                    System.out.printf("Deposited. New balance: %.2f%n", acc.getBalance());
                    break;
                case "3":
                    withdraw(acc);
                    break;
                case "4":
                    showHistory(acc);
                    break;
                case "5":
                    changePin(acc);
                    break;
                case "6":
                    System.out.println("Logged out. Please take your card.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void withdraw(Account acc) {
        double amt = askAmount("Withdraw amount: ");
        if (amt % NOTE_MULTIPLE != 0) {
            System.out.println("Amount must be a multiple of " + (int) NOTE_MULTIPLE + ".");
        } else if (amt > MAX_WITHDRAW) {
            System.out.println("Limit per transaction is " + (int) MAX_WITHDRAW + ".");
        } else if (!acc.withdraw(amt)) {
            System.out.println("Insufficient balance.");
        } else {
            System.out.printf("Please collect your cash. New balance: %.2f%n", acc.getBalance());
        }
    }

    private void showHistory(Account acc) {
        System.out.println("\n--- Transaction History ---");
        List<Transaction> list = acc.getHistory();
        if (list.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        for (Transaction t : list) System.out.println(t);
    }

    private void changePin(Account acc) {
        String oldPin = ask("Current PIN: ");
        String newPin = askNewPin("New 4-digit PIN: ");
        if (acc.changePin(oldPin, newPin)) {
            System.out.println("PIN changed successfully.");
        } else {
            System.out.println("Current PIN is incorrect.");
        }
    }
}

/* ---------------------------------------------------------
   Main entry point
   --------------------------------------------------------- */
public class ATMSimulator {
    public static void main(String[] args) {
        Bank bank = new Bank();

        // Demo account so you can test immediately
        bank.addAccount(new Account("100000000001", "Demo User", "1234", 5000));
        System.out.println("Demo card: 100000000001 | PIN: 1234");

        new ATM(bank).start();
    }
}
