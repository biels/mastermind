package com.mastermind.ui.terminal;

import java.io.IOException;
import java.util.Scanner;

public final class ConsoleUtils {
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String BUNDERSCORE = "\u001B[4m";
    public static final String BLINK = "\u001B[5m";

    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";

    public static void requestEnter(Scanner sc){
        System.out.println("Press enter to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static RequestOptionResult requestOption(Scanner sc, int count) {
        RequestOptionResult result = new RequestOptionResult();
        Integer option = null;
        while (true) {
            String next = sc.next();
            if(next.endsWith("q")) {
                result.setAdditionalAction(RequestOptionResult.AdditionalAction.QUIT);
                return result;
            }
            if(next.equals("d")) {
                result.setAdditionalAction(RequestOptionResult.AdditionalAction.OPEN_JAVADOC);
                return result;
            }
            if(next.endsWith("d")) {
                result.setAdditionalAction(RequestOptionResult.AdditionalAction.OPEN_JAVADOC);
                next = next.substring(0, next.length() - 1);
            }
            try {
                option = Integer.parseUnsignedInt(next);
                option--;
            } catch (NumberFormatException e) {
                System.out.println("Enter a numeric value or an option character");
                continue;
            }
            if (option >= count) {
                System.out.println("Choose an option between 0 and " + (count - 1));
                continue;
            }
            result.setOption(option);
            return result;

        }
    }
    public static class RequestOptionResult{
        enum AdditionalAction {NONE, OPEN_JAVADOC, QUIT}
        private AdditionalAction additionalAction = AdditionalAction.NONE;
        private Integer option = null;

        public AdditionalAction getAdditionalAction() {
            return additionalAction;
        }

        public void setAdditionalAction(AdditionalAction additionalAction) {
            this.additionalAction = additionalAction;
        }

        public Integer getOption() {
            return option;
        }

        public void setOption(Integer option) {
            this.option = option;
        }
    }
}
