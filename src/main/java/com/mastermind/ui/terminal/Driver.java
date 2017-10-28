package com.mastermind.ui.terminal;

import com.mastermind.ui.terminal.ConsoleUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Stream;

public class Driver {
    private Class<?> clazz;
    private Object service;

    public Driver(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void instantiateService() {
        if (service != null) return;
        try {
            service = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void interactiveMenu(Scanner sc) {
        boolean needReprint = true;
        String b = ConsoleUtils.BOLD;
        String r = ConsoleUtils.RESET;
        while (true) {
            Method[] methods = new Method[0];
            if (needReprint) {
                System.out.println(MessageFormat.format("Driver for {0}{1}{2}: (q to quit)",
                        "", clazz.getSimpleName(), ConsoleUtils.RESET));
                methods = clazz.getDeclaredMethods();
                for (int i = 0; i < methods.length; i++) {
                    Method method = methods[i];
                    System.out.println(MessageFormat.format(" " + ConsoleUtils.RESET + "{0})" + ConsoleUtils.BOLD + " {1}" + ConsoleUtils.RESET,
                            i + 1, method.getName()));
                }
            }
            ConsoleUtils.RequestOptionResult result = ConsoleUtils.requestOption(sc, methods.length);
            if (result.getAdditionalAction() == ConsoleUtils.RequestOptionResult.AdditionalAction.QUIT) return;
            if(result.getAdditionalAction() == ConsoleUtils.RequestOptionResult.AdditionalAction.OPEN_JAVADOC) {
                needReprint = false;
                DocumentationUtils.openDocumentationFor(clazz);
            }
            if(result.getOption() != null){
                Method selected = methods[result.getOption()];
                Optional<String> parameterString = Arrays.stream(selected.getParameters())
                        .map(parameter -> parameter.toString())
                        .reduce((s1, s2) -> s1 + ", " + s2);
                System.out.println("Parameters: " + parameterString);
                needReprint = true;
                ConsoleUtils.requestEnter(sc);
            }

//        try {
//            selected.invoke(service);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
        }
    }

    public String getName() {
        return clazz.getSimpleName();
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
