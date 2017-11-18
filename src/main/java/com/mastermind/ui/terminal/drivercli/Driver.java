package com.mastermind.ui.terminal.drivercli;

import com.mastermind.ui.terminal.ConsoleUtils;

import java.io.Serializable;
import java.lang.reflect.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    public void interactiveMenu(Scanner sc, boolean interactive) {
        boolean needReprint = true;
        boolean needPause = true;
        String b = ConsoleUtils.BOLD;
        String r = ConsoleUtils.RESET;
        while (sc.hasNext()) {
            List<Method> methods;
            methods = Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> Modifier.isPublic(method.getModifiers()))
                    .sorted(Comparator.comparing(Method::getName))
                    .collect(Collectors.toList());
            if (needReprint) {
                System.out.println(MessageFormat.format("Driver for {0}{1}{2}: (q to quit)",
                        "", clazz.getSimpleName(), ConsoleUtils.RESET));
                for (int i = 0; i < methods.size(); i++) {
                    Method method = methods.get(i);
                    System.out.println(MessageFormat.format(" " + ConsoleUtils.RESET + "{0})" + ConsoleUtils.BOLD + " {1}" + ConsoleUtils.RESET,
                            i + 1, method.getName()));
                }
            }
            ConsoleUtils.RequestOptionResult result = ConsoleUtils.requestOption(sc, methods.size());
            if (result.getAdditionalAction() == ConsoleUtils.RequestOptionResult.AdditionalAction.QUIT) return;
            if (result.getAdditionalAction() == ConsoleUtils.RequestOptionResult.AdditionalAction.OPEN_JAVADOC) {
                needReprint = false;
                DocumentationUtils.openDocumentationFor(clazz);
            }
            if (result.getOption() != null) {
                Method selected = methods.get(result.getOption());
                Parameter[] parameters = selected.getParameters();
                Optional<String> parameterString = Arrays.stream(parameters)
                        .map(Parameter::toString)
                        .reduce((s1, s2) -> s1 + ", " + s2);
                System.out.println("Parameters: " + parameterString);
                Object[] resolvedParams = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    Object res = null;
                    Class<?> type = null;
                    boolean requested = false;
                    while (true) {
                        try {
                            System.out.print(parameter.getType().getSimpleName() + " " + parameter.getName() + ": ");
                            res = null;
                            type = parameter.getType();
                            requested = false;
                            if (type == boolean.class || type == Boolean.class) {
                                requested = true;
                                res = sc.nextBoolean();
                            }
                            if (type == int.class || type == Integer.class) {
                                requested = true;
                                res = sc.nextInt();
                            }
                            if (type == long.class || type == Long.class) {
                                requested = true;
                                res = sc.nextBigInteger().longValue();
                            }
                            if (type == String.class) {
                                requested = true;
                                res = sc.next();
                            }
                            break;
                        } catch (InputMismatchException e) {
                            sc.next();
                            System.out.println(" > Not a " + parameter.getType().getSimpleName());
                        }
                    }
                    if (!requested)
                        System.out.println("(No input method for " + type.getSimpleName() + ")");
                    resolvedParams[i] = res;
                }
                try {
                    Object invokeResult = selected.invoke(service, resolvedParams);
                    if (selected.getReturnType().equals(Void.TYPE)) {
                        needPause = false;
                        System.out.println("Executed (void)");
                    } else {
                        System.out.println("Result (" + selected.getReturnType() + "):");
                        System.out.print(ConsoleUtils.BOLD);
                        System.out.println(getStringRepresentation(invokeResult, 0));
                        System.out.print(ConsoleUtils.RESET);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    System.out.println("Result: (a " + e.getTargetException().getClass().getSimpleName() + " was thrown)");
                    e.getTargetException().printStackTrace();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

                needReprint = true;
                if (needPause && interactive) ConsoleUtils.requestEnter(sc);
                needPause = true;
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

    public static String getStringRepresentation(Object object, int depth) throws IllegalAccessException {
        if (object == null) return "null";
        if (depth > 6) return object.toString();
        Class<?> c = object.getClass();
        if (c.isPrimitive()) return object.toString();
        if (object instanceof Collection) {
            Object cString = ((Collection) object).stream().map(o -> {
                try {
                    return getStringRepresentation(o, 0);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return "";
            }).reduce((s1, s2) -> s1 + ", " + s2).orElse("");
            String lines;
            if (cString.equals("")) return "[]";
            else lines = "[\n" + cString;
            return indent(lines) + "]";
        }
        if (object instanceof Serializable) return object.toString();
        //if(c == Integer.class || c == Double.class || c == Long.class || c == Boolean) return object.toString();
        if (Arrays.stream(c.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .count() <= 0) return object.toString();
        String header = c.getSimpleName() + " {\n";
        String footer = "}";

        List<Field> fields = getAllFields(c);
        String resultRepresentation = header;
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            if (Modifier.isStatic(field.getModifiers())) continue;
            field.setAccessible(true);
            resultRepresentation += INDENT + field.getName() + " = ";
            String stringRepresentation = getStringRepresentation(field.get(object), depth + 1);
            resultRepresentation += indent(stringRepresentation);
        }
        resultRepresentation += footer;
        return resultRepresentation;
    }

    final static String INDENT = "  ";

    private static String indent(String lines) {

        String result = "";
        String[] split = lines.split("\n");

        for (int j = 0; j < split.length; j++) {
            result += (j != 0 ? INDENT : "") + split[j] + "\n";
        }
        return result;
    }

    public static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

}
