package com.mastermind.ui.terminal;

import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.impl.RepositoriesInMemoryImpl;
import com.mastermind.services.players.PlayersService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        RepositoryManager.attatchImplementation(new RepositoriesInMemoryImpl());
        PlayersService playersService = new PlayersService();
        Scanner scanner = new Scanner(System.in);

        DriverManager driverManager = new DriverManager();

        driverManager.interactiveMenu(scanner);
    }
}
