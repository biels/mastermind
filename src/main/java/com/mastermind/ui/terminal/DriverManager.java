package com.mastermind.ui.terminal;

import com.mastermind.services.game.GameService;
import com.mastermind.services.players.PlayersService;
import com.mastermind.services.ranking.RankingService;

import java.text.MessageFormat;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DriverManager {

    private final List<Driver> driverList;
    public DriverManager() {
        driverList = Stream.of(
                PlayersService.class,
                GameService.class,
                RankingService.class
        )
                .map(Driver::new)
                .collect(Collectors.toList());
    }

    public void interactiveMenu(Scanner sc) {
        while (true) {
            System.out.println("Choose a driver: (q to quit)");
            List<Driver> drivers = driverList;
            for (int i = 1; i < drivers.size(); i++) {
                Driver driver = drivers.get(i);
                System.out.println(MessageFormat.format(
                        " " + ConsoleUtils.RESET + "{0})" + ConsoleUtils.BOLD + " {1}", i, driver.getName()));
            }
            Integer option = ConsoleUtils.requestOption(sc, drivers.size());
            if(option == null)return;
            Driver driver = drivers.get(option);
            driver.instantiateService();
            driver.interactiveMenu(sc);
        }
    }


}
