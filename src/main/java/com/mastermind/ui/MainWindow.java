package com.mastermind.ui;

import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.impl.RepositoriesInMemoryImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class MainWindow extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Window");
        Parent root = FXMLLoader.load(getClass().getResource("/sample_window.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args){
        // Initialize repositories with an in-memory implementation
        RepositoryManager.attatchImplementation(new RepositoriesInMemoryImpl());
        // Launch JavaFX application
        Application.launch(args);
    }
}
