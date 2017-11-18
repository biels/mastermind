package com.mastermind.ui.terminal.drivercli;

import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.impl.RepositoriesInMemoryImpl;
import com.mastermind.ui.terminal.ConsoleUtils;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class DriverCLI {
    public static void main(String[] args) {
        RepositoryManager.attachImplementation(new RepositoriesInMemoryImpl());
        Scanner scanner = new Scanner(System.in);
        DriverManager driverManager = new DriverManager();
        ConsoleUtils.printBanner();
        try {
            driverManager.interactiveMenu(scanner);
        } catch (NoSuchElementException e) {
        }
    }
}
