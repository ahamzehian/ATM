package com.amirportfolio;

import com.amirportfolio.database.DataManager;
import com.amirportfolio.interaction.Interact;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Main {

    public static void main(String[] args) {

        DataManager dataManager = new DataManager();
//
//        String name = "Lily";
//        int accountNumber = 123456;
//        dataManager.setupClientDatabase(accountNumber);
//        dataManager.addClient(name,accountNumber,"2244Test");
//
//        name = "Ariel";
//        accountNumber = 7891011;
//        dataManager.setupClientDatabase(accountNumber);
//        dataManager.addClient(name,accountNumber,"3366Ariel");
//
//        name = "Julia";
//        accountNumber = 246810;
//        dataManager.setupClientDatabase(accountNumber);
//        dataManager.addClient(name,accountNumber,"Julia1234");

//        System.out.println(dataManager.getListOfAccounts());

//        dataManager.deleteAccountTables();
//        dataManager.clearClientsTable();

        Interact interact = new Interact();
        interact.launchTheApp();
//        interact.showHelp();

    }
}
