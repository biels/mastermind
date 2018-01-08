package com.mastermind.ui.javafx;

import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.impl.RepositoriesInMemoryImpl;
import com.mastermind.services.ServiceManager;
import com.mastermind.services.game.GameService;
import com.mastermind.services.login.LoginService;
import com.mastermind.services.players.PlayersService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Stack;


public class Application extends javafx.application.Application {
    public static Application instance;
    {
        instance = this;
    }

    private Stack<Fragment> fragmentStack = new Stack<>();

    private Stage primaryStage;
    private RegisterFragment registerFragment = new RegisterFragment();
    private LoginFragment loginFragment = new LoginFragment();
    private GameFragment gameFragment = new GameFragment();
    // References
    private Pane pnlContent;
    private Button btnBack;
    private Button btnNewGame;
    private Label lblLoggedIn;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Window");
        Parent root = FXMLLoader.load(getClass().getResource("/main_window.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(600);
        primaryStage.show();

        // References
        pnlContent = (Pane) primaryStage.getScene().lookup("#pnlContent");
        btnBack = (Button) primaryStage.getScene().lookup("#btnBack");
        btnNewGame = (Button) primaryStage.getScene().lookup("#btnNewGame");
        lblLoggedIn = (Label) primaryStage.getScene().lookup("#lblLoggedIn");

        // Handlers
        btnBack.setOnAction(event -> actionBack());
        btnNewGame.setOnAction(event -> actionNewGame());

        // Startup actions
        startFromFragment(gameFragment);
        pushFragment(loginFragment);
        updateLoggedInLabel();
    }

    private void actionBack(){
        popFragment();
    }
    private void actionNewGame(){
        // Show new game fragment

        ServiceManager.getGameService().newGame(1);
    }
    public void updateLoggedInLabel(){
        LoginService loginService = ServiceManager.getLoginService();
        String loggedInText = "Not logged in";
        if (loginService.isLoggedIn()) {
             loggedInText = "Logged in as " + loginService.getLoggedInPlayerName();
        }
        lblLoggedIn.setText(loggedInText);
    }
    // Fragments
    public void pushFragment(Fragment fragment){
        if(isCurrentFragment(fragment.getClass())){
            swapFragment(fragment);
            return;
        }
        fragmentStack.push(fragment);
        displayTopOfStack();
    }
    public void popFragment(){
        if(fragmentStack.size() <= 1)return;
        fragmentStack.pop();
        displayTopOfStack();
    }
    public void swapFragment(Fragment fragment){
        fragmentStack.pop();
        pushFragment(fragment);
    }
    public void startFromFragment(Fragment fragment){
        fragmentStack.clear();
        pushFragment(fragment);
    }
    private void displayTopOfStack(){
        Fragment fragment = fragmentStack.lastElement();
        displayFragment(fragment);
    }
    private void displayFragment(Fragment fragment) {
        fragment.displayIn(pnlContent);
    }
    private boolean isCurrentFragment(Class<? extends Fragment> clazz){
        if(fragmentStack.empty()) return false;
        return fragmentStack.lastElement().getClass().isAssignableFrom(clazz);
    }


    public static void main(String[] args) {
        // Initialize repositories with an in-memory implementation
        RepositoryManager.attachImplementation(new RepositoriesInMemoryImpl());
        // Launch JavaFX application
        javafx.application.Application.launch(args);
    }
}
