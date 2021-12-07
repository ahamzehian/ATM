package com.amirportfolio.interaction;

import com.amirportfolio.account.Account;
import com.amirportfolio.database.DataManager;
import com.amirportfolio.database.DataManager2;

import java.text.DecimalFormat;
import java.util.*;

public class Interact {

    private final DataManager2 dataManager = new DataManager2();
    private int accountNumber;
    private Scanner scanner = new Scanner(System.in);
    private Scanner scanInt = new Scanner(System.in);
    private Account account = new Account();

    private Map<String,String> commandsMap = new HashMap<>(Map.of(
            "pas", "Change password.",
            "dep", "Deposit.",
            "wth", "Withdraw.",
            "rem", "Get remainder.",
            "stm", "Get statement of an account."));

    private Map<String, String> mainCommandMap = new HashMap<>(Map.of(
            "acc","Access existing account.",
            "add", "Add new client."));

    public Interact() {

    }

    public void launchTheApp() {

        boolean continueTheApp = true;

        while(continueTheApp) {
            chooseMainCommand();
            continueTheApp = askToContinue();
        }

        dataManager.closeDataManager();

    }

    public void chooseMainCommand() {
        showHelp(mainCommandMap);
        System.out.println("Choose your command: ");
        String cmd = scanner.nextLine();

        switch (cmd) {
            case "acc":
                accessExistingAccount();
                break;

            case "add":
                addNewClient();
                break;

            default:
                break;
        }
    }

    public boolean askToContinue() {
        List<String> acceptableAnswer = List.of("y","n");
        System.out.println("Do you wish to continue? y/n");
        String answer = scanner.nextLine();
        answer = answer.toLowerCase();

        while (!acceptableAnswer.contains(answer)) {
            System.out.println("Unacceptable input please use only 'y' or 'n'.");
            System.out.println("Do you wish to continue? y/n");
            answer = scanner.nextLine();
        }

        return answer.equals("y");
    }

    public void accessExistingAccount() {

        System.out.println("Enter account number: ");
        int accountNumber = scanInt.nextInt();

        account.setAccountNumber(accountNumber);

        boolean repeat = true;

        while (repeat) {
            chooseExistingAccountCommands();
            repeat = askToRepeatMethod();
        }

    }

    public void chooseExistingAccountCommands() {
        showHelp(commandsMap);
        System.out.println("Choose your command: ");
        String cmd = scanner.nextLine();

        switch (cmd) {
            case "pas":
                changePassword();
                break;

            case "dep":
                depositFunction();
                break;

            case "wth":
                withdrawFunction();
                break;

            case "rem":
                getRemainderFunction();
                break;

            case "stm":
                getAccountStatement();
                break;

            default:
                break;
        }
    }

    public boolean askToRepeatMethod() {
        List<String> acceptableAnswer = List.of("y","n");
        System.out.println("Do you wish to repeat the last command? y/n");
        String answer = scanner.nextLine();
        answer = answer.toLowerCase();

        while (!acceptableAnswer.contains(answer)) {
            System.out.println("Unacceptable input please use only 'y' or 'n'.");
            System.out.println("Do you wish to repeat the last command? y/n");
            answer = scanner.nextLine();
        }

        return answer.equals("y");
    }

    public boolean repeatAnswerIsValid(String answer) {
        List<String> answers = new ArrayList<>(List.of("y","n"));
        answer = answer.toLowerCase(Locale.ROOT);
        return answers.contains(answer);
    }

    public String getFunctionInput(Map<String,String> commands) {

        System.out.println("Enter function: ");
        String function = scanner.nextLine();

        boolean repeat = !functionIsValid(commands,function);

        while (repeat) {
            System.out.println("Please use one of the following commands.");
            showHelp(commands);
            function = scanner.nextLine();
            repeat = functionIsValid(commands,function);
        }

        return function;

    }

    public boolean functionIsValid(Map<String,String> commands, String inputFunction) {

        List<String> command = new ArrayList<>(commands.keySet());
        inputFunction = inputFunction.toLowerCase(Locale.ROOT);
        return !command.contains(inputFunction);

    }

    public void showHelp(Map<String, String> cmdMap) {
        int i = 1;
        System.out.println("---+ command +-----------------");
        for (String key:cmdMap.keySet()) {
            System.out.printf("%d. | %7s | %16s\n",i,key,cmdMap.get(key));
            i++;
        }
    }

    // Functions --------------------------------------------------------------------------

    public void addNewClient() {
        account = new Account();

        System.out.println("Enter name: ");
        String name = scanner.nextLine();
        account.setName(name);

        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        account.setPassword(password);

        System.out.println("Enter account number: ");
        int accountNumber = scanInt.nextInt();
        account.setAccountNumber(accountNumber);

        dataManager.addClient(account.getName(),account.getAccountNumber(),account.encrypt(account.getPassword()));
    }

    public void changePassword() {
        System.out.println("Enter your current password: ");
        String oldPass = scanner.nextLine();

        if (passwordIsValid(account.encrypt(oldPass))) {
            System.out.println("Enter new password: ");
            String newPass = scanner.nextLine();

            System.out.println("Enter new password one more time: ");
            String newPassRepeat = scanner.nextLine();

            while (!newPass.equals(newPassRepeat)) {
                System.out.println("inputs do not match! Please try again.");
                System.out.println("Enter new password: ");
                newPass = scanner.nextLine();

                System.out.println("Enter new password one more time: ");
                newPassRepeat = scanner.nextLine();
            }

            dataManager.updatePassword(account.getAccountNumber(), account.encrypt(newPass));

            System.out.println("Password has updated.");
        } else {
            System.out.println("Incorrect password!");
        }
    }

    public void depositFunction() {
        System.out.println("Enter amount: ");
        double amount = scanInt.nextDouble();
        if (amount < 0) {
            amount *= -1;
        }
        dataManager.recordTransaction(account.getAccountNumber(), amount);
        System.out.println("Transaction recorded.");
    }

    public void withdrawFunction() {
        System.out.println("Enter amount: ");
        double amount = scanInt.nextDouble();
        if (amount < 0) {
            amount *= -1;
        }
        if (dataManager.getRemainingAmount(account.getAccountNumber()) > amount) {
            dataManager.recordTransaction(account.getAccountNumber(),amount*-1);
            System.out.println("Transaction recorded.");
        } else {
            System.out.println("Insufficient amount!");
        }
    }

    public void getRemainderFunction() {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        System.out.println("Remaining: " +
                formatter.format(dataManager.getRemainingAmount(account.getAccountNumber())));
    }

    public void getAccountStatement() {
        List<String> accountStatement = dataManager.getListOfTransactions(account.getAccountNumber());
        for (String tran:accountStatement) {
            System.out.println(tran);
        }
    }

    // Function support methods -----------------------------------------------------------

    public boolean passwordIsValid(String password) {
        return dataManager.checkPassword(account.getAccountNumber(), account.encrypt(password));
    }

}
