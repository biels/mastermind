package com.mastermind.ui.javafx;

import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.impl.RepositoriesInMemoryImpl;
import com.mastermind.services.ServiceManager;
import com.mastermind.services.login.LoginService;
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
    private Button btnPrimaryAction;
    private Label lblLoggedIn;
    // State
    private Action primaryAction;
    private boolean inGame;
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
        btnPrimaryAction = (Button) primaryStage.getScene().lookup("#btnPrimaryAction");
        lblLoggedIn = (Label) primaryStage.getScene().lookup("#lblLoggedIn");

        // Handlers
        btnBack.setOnAction(event -> actionBack());
        btnPrimaryAction.setOnAction(event -> primaryAction());

        // Startup actions
        startFromFragment(gameFragment);
        pushFragment(loginFragment);

        // Initial events
        onLoggedInChanged();
    }

    // Actions
    public enum Action {
        BACK(instance::actionNewGame, "Back"),
        LOGIN(instance::actionLogin, "Log in"),
        NEW_GAME(instance::actionNewGame, "New Game");

        private final String displayName;
        private final Runnable handler;

        Action(Runnable handler, String displayName) {
            this.displayName = displayName;
            this.handler = handler;
        }

        Action(Runnable handler) {
            this.handler = handler;
            this.displayName = this.name();
        }

        public String getDisplayName() {
            return displayName;
        }

        public Runnable getHandler() {
            return handler;
        }

        public void execute() {
            handler.run();
        }
    }
    private void actionBack(){
        popFragment();
    }
    private void actionLogin(){
        pushFragment(new LoginFragment());
    }
    private void actionNewGame(){
        // Show new game fragment

        ServiceManager.getGameService().newGame(1);
    }
    public void onLoggedInChanged(){
        LoginService loginService = ServiceManager.getLoginService();
        String loggedInText = "Not logged in";
        if (loginService.isLoggedIn()) {
             loggedInText = "Logged in as " + loginService.getLoggedInPlayerName();
        }
        lblLoggedIn.setText(loggedInText);
        updatePrimaryAction();
    }
    public void primaryAction(){
        primaryAction.execute();
    }
    public void setPrimaryAction(Action primaryAction) {
        btnPrimaryAction.setText(primaryAction.getDisplayName());
        this.primaryAction = primaryAction;
    }
    public void updatePrimaryAction(){
        LoginService loginService = ServiceManager.getLoginService();
        if(!loginService.isLoggedIn()) {
            setPrimaryAction(Action.LOGIN);
            return;
        }
        if(!inGame) {
            setPrimaryAction(Action.NEW_GAME);
            return;
        }
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
