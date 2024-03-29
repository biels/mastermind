package com.mastermind.ui.terminal.drivercli;

import com.mastermind.services.game.GameService;
import com.mastermind.services.login.LoginService;
import com.mastermind.services.players.PlayersService;
import com.mastermind.services.ranking.RankingService;
import com.mastermind.ui.terminal.ConsoleUtils;

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
                RankingService.class,
                LoginService.class
        )
                .map(Driver::new)
                .collect(Collectors.toList());
    }

    public void interactiveMenu(Scanner sc) {
        boolean interactive = true;
        boolean needReprint = true;
        String b = ConsoleUtils.BOLD;
        String r = ConsoleUtils.RESET;
        System.out.println("Mastermind driver CLI v" + "1.0");
        System.out.println("Usage (all menus):");
        System.out.println(" " + b + "<option>" + r + ": choose option");
        System.out.println(" " + b + "d" + r + ": open documentation");
        System.out.println(" " + b + "<option>d" + r + ": open documentation for option");
        System.out.println(" " + b + "q" + r + ": quit or go back");
        System.out.println(" " + b + "ni" + r + ": enable non-interactive mode");
        while (interactive || sc.hasNext()) {
            List<Driver> drivers = null;
            drivers = driverList;
            if (needReprint) {
                System.out.println("Choose a driver: (q to quit)");
                for (int i = 0; i < drivers.size(); i++) {
                    Driver driver = drivers.get(i);
                    System.out.println(MessageFormat.format(
                            " " + ConsoleUtils.RESET + "{0})" + ConsoleUtils.BOLD + " {1}" + ConsoleUtils.RESET, i + 1, driver.getName()));
                }
            }
            ConsoleUtils.RequestOptionResult result = ConsoleUtils.requestOption(sc, drivers.size());
            if (result.getAdditionalAction() == ConsoleUtils.RequestOptionResult.AdditionalAction.QUIT) return;
            if (result.getAdditionalAction() == ConsoleUtils.RequestOptionResult.AdditionalAction.ENABLE_NON_INTERACTIVE_MODE) {
                interactive = false;
                System.out.println("Now running in non-interactive mode.");
            }
            if (result.getOption() != null) {
                Driver driver = drivers.get(result.getOption());

                if (result.getAdditionalAction() == ConsoleUtils.RequestOptionResult.AdditionalAction.OPEN_JAVADOC) {
                    DocumentationUtils.openDocumentationFor(driver.getClazz());
                    needReprint = false;
                    continue;
                }
                driver.instantiateService();
                needReprint = true;
                driver.interactiveMenu(sc, interactive);
            } else {
                if (result.getAdditionalAction() == ConsoleUtils.RequestOptionResult.AdditionalAction.OPEN_JAVADOC) {
                    System.out.println("No documentation for context. Try <option>d.");
                    needReprint = false;
                    continue;
                }
            }
        }
    }


}
