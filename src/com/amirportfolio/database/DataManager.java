package com.amirportfolio.database;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private final String DB_PATH = "C:\\Users\\amirh\\Desktop\\SQL\\ATM";
    private final String DB_NAME = "accountInfo.db";
    private final String connectionCommand = "jdbc:sqlite:" + DB_PATH + "\\" + DB_NAME;

    private final String NAME_COLUMN = "name";
    private final String ACCOUNT_COLUMN = "account_number";
    private final String PASSWORD_COLUMN = "password";
    private final String DATE_COLUMN = "transaction_date";
    private final String AMOUNT_COLUMN = "transaction_amount";
    private final String REMAINING_COLUMN = "remain_amount";

    public DataManager() {
        setupDb();
    }


    /**
     * Prepare database and create clients table if it does not exist
     */
    private void setupDb() {
        try (Connection connection = DriverManager.getConnection(connectionCommand)) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS clients(_id INT PRIMARY KEY, " + NAME_COLUMN +
                    " TEXT, " + ACCOUNT_COLUMN + " REAL, " + PASSWORD_COLUMN + " TEXT)");
            System.out.println("Clients database is ready.");
        } catch (SQLException e) {
            System.out.println("Failed to create database! " + e.getMessage());
        }
    }


    /**
     * Creates a table with following formula for the table name: name_accountNumber
     */
    public void setupClientDatabase(int accountNumber) {
        try(Connection connection = DriverManager.getConnection(connectionCommand)){

            Statement statement = connection.createStatement();
            String tableName = "t" + accountNumber;

            statement.execute("CREATE TABLE IF NOT EXISTS " + tableName + "(_id INT PRIMARY KEY, " +
                    ACCOUNT_COLUMN + " INT, " + DATE_COLUMN + " TEXT, " + AMOUNT_COLUMN + " INT, " +
                    REMAINING_COLUMN + " INT)");

            System.out.println("table for " + accountNumber + " is ready.");

        } catch (SQLException e) {
            System.out.println("Failed to add client! " + e.getMessage());
        }
    }


    public void addClient(String name, int accountNumber, String password) {
        try (Connection connection = DriverManager.getConnection(connectionCommand)) {

            Statement statement = connection.createStatement();

            int id = getLastId("clients") + 1;
            statement.execute("INSERT INTO clients VALUES(" + id + ", '" + name + "'," + accountNumber +
                    ",'" + password + "')");

            setupClientDatabase(accountNumber);

            System.out.println("Data recorded. | " + name + ", " + accountNumber + ", " + password);

        } catch (SQLException e) {
            System.out.println("Failed to record the data! " + e.getMessage());
        }
    }


    public int getAccountNumber(String name) {
        try (Connection connection = DriverManager.getConnection(connectionCommand)) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT " + ACCOUNT_COLUMN + " FROM clients " +
                    "WHERE " + NAME_COLUMN + "=" + name);

            return resultSet.getInt(ACCOUNT_COLUMN);

        } catch (SQLException e) {
            System.out.println("Failed to get account number! " + e.getMessage());
            return 0;
        }
    }


    public boolean accountNumberExists(int accountNumber) {
        try (Connection connection = DriverManager.getConnection(connectionCommand)) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(" + ACCOUNT_COLUMN +
                    ") AS accountNumberCount FROM clients WHERE " + ACCOUNT_COLUMN + "=" + accountNumber);

            return resultSet.getInt("accountNumberCount")!=0;

        } catch (SQLException e) {
            System.out.println("Failed to check account number! " + e.getMessage());
            return false;
        }
    }


    /**
     * Return the last id number of the given table
     */
    public int getLastId(String tableName) {
        if (!tableIsEmpty(tableName)) {
            try (Connection connection = DriverManager.getConnection(connectionCommand)) {

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT MAX(_id) AS ind FROM clients");

                return resultSet.getInt("ind");

            } catch (SQLException e) {
                System.out.println("Failed to get the last id! " + e.getMessage());
                return 0;
            }
        } else {
            return 0;
        }
    }

    public boolean tableIsEmpty(String tableName) {

        try (Connection connection = DriverManager.getConnection(connectionCommand)) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS qty FROM " + tableName);

            return resultSet.getInt("qty")==0;

        } catch (SQLException e) {
            System.out.println("Failed to check if table is empty! " + e.getMessage());
            return false;
        }

    }

    public List<Integer> getListOfAccountNumbers() {

        List<Integer> accountsList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(connectionCommand)) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT " + ACCOUNT_COLUMN +
                    " FROM clients");

            while (resultSet.next()) {
                accountsList.add(resultSet.getInt(ACCOUNT_COLUMN));
            }

            return accountsList;

        } catch (SQLException e) {
            System.out.println("Failed to get list of accounts! " + e.getMessage());
            return accountsList;
        }
    }

    public void changeAccountNumber(String name, int accountNumber) {

        try (Connection connection = DriverManager.getConnection(connectionCommand)) {

            Statement statement = connection.createStatement();
            statement.execute("UPDATE clients SET " + ACCOUNT_COLUMN + "=" + accountNumber +
                    " WHERE " + NAME_COLUMN + "=" + name);

        } catch (SQLException e) {
            System.out.println("Failed to replace account number! " + e.getMessage());
        }

    }

    /**
     * Delete the rows in clients table
     */
    public void clearClientsTable() {
        try (Connection connection = DriverManager.getConnection(connectionCommand)) {

            Statement statement = connection.createStatement();

            statement.execute("DELETE FROM clients WHERE TRUE");

            System.out.println("database is cleared!");
            statement.close();
        } catch (SQLException e) {
            System.out.println("Failed to clear the database! " + e.getMessage());
        }
    }

    public void deleteAccountTables() {
        try (Connection connection = DriverManager.getConnection(connectionCommand)) {

            Statement statement = connection.createStatement();

            if (!getListOfAccountNumbers().isEmpty()) {
                for (int accountNumber : getListOfAccountNumbers()) {
                    statement.execute("DROP TABLE t" + accountNumber);
                }
            }

            System.out.println("All account tables are deleted!");

        } catch (SQLException e) {
            System.out.println("Failed to delete account tables! " + e.getMessage());
        }
    }


    /**
     * Check if given name exists in clients database
     */
    public boolean checkNameExists(String name) {
        try (Connection connection = DriverManager.getConnection(connectionCommand)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (" + NAME_COLUMN +
                    ") AS nameQty FROM clients WHERE " + NAME_COLUMN + "='" + name + "'");
            return resultSet.getInt("nameQty")>0;
        } catch (SQLException e) {
            System.out.println("Failed to retrieve account info related to " + name + "! " + e.getMessage());
            return false;
        }
    }

    // update password by using only name of the client
    public void updatePassword(String name, String password) {
        try (Connection connection = DriverManager.getConnection(connectionCommand)) {

            Statement statement = connection.createStatement();
            statement.execute("UPDATE clients SET " + PASSWORD_COLUMN + "=" + password +
                    " WHERE " + NAME_COLUMN + "=" + name);

        } catch (SQLException e) {
            System.out.println("Failed to change new password! " + e.getMessage());
        }
    }
    // update password by using only account number
    public void updatePassword(int accountNumber, String password) {
        try (Connection connection = DriverManager.getConnection(connectionCommand)) {

            Statement statement = connection.createStatement();
            statement.execute("UPDATE clients SET " + PASSWORD_COLUMN + "=" + password +
                    " WHERE " + ACCOUNT_COLUMN + "=" + accountNumber);

        } catch (SQLException e) {
            System.out.println("Failed to change new password! " + e.getMessage());
        }
    }

    public boolean checkPassword(int account, String encryptedPassword) {
        try (Connection connection = DriverManager.getConnection(connectionCommand)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT " + PASSWORD_COLUMN +
                    " FROM clients WHERE " + ACCOUNT_COLUMN + "='" + account + "'");
            return encryptedPassword.equals(resultSet.getString(PASSWORD_COLUMN));
        } catch (SQLException e) {
            System.out.println("Failed to get the password! " + e.getMessage());
            return false;
        }
    }

    /**
     * Return the last recorded remainder in an account
     */
    public int getRemainingAmount(int accountNumber) {

//        System.out.println("From getRemainingAmount: " + accountNumber);
        String tableName = "t" + accountNumber;
        if (!tableIsEmpty(tableName)) {
            try (Connection connection = DriverManager.getConnection(connectionCommand)) {

                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT " + REMAINING_COLUMN +
                            " FROM " + tableName + " WHERE _id=" + getLastId(tableName));

                return resultSet.getInt(REMAINING_COLUMN);

            } catch (SQLException e) {
                System.out.println("Failed to get remaining amount! " + e.getMessage());
                return 0;
            }
        } else {
            return 0;
        }
    }


    public void recordTransaction(int accountNumber, int transaction) {
        try (Connection connection = DriverManager.getConnection(connectionCommand)) {

            Statement statement = connection.createStatement();
            String tableName = "t" + accountNumber;

            int id = getLastId(tableName) + 1;
            int remainder = getRemainingAmount(accountNumber) + transaction;

            // Get current time and date of transaction
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            String recordedDate = formatter.format(date);

            statement.execute("INSERT INTO "  + tableName + " VALUES(" +
                    id + "," + accountNumber + ",'" + recordedDate + "'," + transaction + "," + remainder + ")");
            System.out.println("Transaction recorded.");
        } catch (SQLException e) {
            System.out.println("Failed to record the transaction! " + e.getMessage());
        }
    }



}
