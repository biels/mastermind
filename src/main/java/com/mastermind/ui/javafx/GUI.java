package com.mastermind.ui.javafx;

import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.impl.RepositoriesInMemoryImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class GUI extends Application {
    private RegisterFragment registerFragment = new RegisterFragment();
    private Pane pnlContent;
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Window");
        Parent root = FXMLLoader.load(getClass().getResource("/main_window.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        displayRegisterFragment(primaryStage);
    }

    private void displayRegisterFragment(Stage primaryStage) {
        registerFragment.loadFXML();
        pnlContent = (Pane) primaryStage.getScene().lookup("#pnlContent");
        pnlContent.getChildren().setAll(registerFragment.getNode());
    }

    public static void main(String[] args) {
        // Initialize repositories with an in-memory implementation
        RepositoryManager.attachImplementation(new RepositoriesInMemoryImpl());
        // Launch JavaFX application
        Application.launch(args);
    }
}
