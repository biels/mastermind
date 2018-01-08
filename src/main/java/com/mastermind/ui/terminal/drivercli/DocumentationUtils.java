package com.mastermind.ui.terminal.drivercli;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DocumentationUtils {
    private static String DOC_PATH = "build/docs/javadoc";

    private static File getDocDirFor(Package aPackage) {
        return new File(DOC_PATH + "/" + aPackage.getName().replace(".", "/"));
    }

    private static File getDocDirFor(Class<?> clazz) {
        return new File(DOC_PATH + "/" + clazz.getName().replace(".", "/") + ".html");
    }

    public static void openDocumentationFor(Class<?> clazz) {
        try {
            Desktop.getDesktop().browse(getDocDirFor(clazz).getAbsoluteFile().toURI());
        } catch (IOException e) {
            System.out.println("Please execute ./gradlew javadoc to generate documentation");
            //e.printStackTrace();
        }
    }
}
