package com.mastermind.ui.terminal;

import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.impl.RepositoriesInMemoryImpl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RepositoryManager.attachImplementation(new RepositoriesInMemoryImpl());
        Scanner scanner = new Scanner(System.in);
        DriverManager driverManager = new DriverManager();
        ConsoleUtils.printBanner();
        driverManager.interactiveMenu(scanner);
    }
}
