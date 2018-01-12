package com.mastermind.ui.javafx;

import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.impl.RepositoriesInMemoryImpl;
import com.mastermind.services.ServiceManager;
import com.mastermind.services.game.GameService;
import com.mastermind.services.login.LoginService;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
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
    private Button btnPrimaryAction, btnSettings;
    private Label lblLoggedIn;
    private MenuBar menuBar;
    private ComboBox cmbSlots;
    private ComboBox cmbColors;
    // State
    private Action primaryAction;
    private boolean inGame;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Window");
        Parent root = FXMLLoader.load(getClass().getResource("/main_window.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(600);
        ChangeListener<Number> numberChangeListener = (observable, oldValue, newValue) -> {
            if (fragmentStack.size() > 0) fragmentStack.lastElement().onResize();
        };
        primaryStage.widthProperty().addListener(numberChangeListener);
        primaryStage.heightProperty().addListener(numberChangeListener);
        primaryStage.show();

        // References
        pnlContent = (Pane) primaryStage.getScene().lookup("#pnlContent");
        btnBack = (Button) primaryStage.getScene().lookup("#btnBack");
        btnSettings = (Button) primaryStage.getScene().lookup("#btnSettings");
        btnPrimaryAction = (Button) primaryStage.getScene().lookup("#btnPrimaryAction");
        lblLoggedIn = (Label) primaryStage.getScene().lookup("#lblLoggedIn");
        menuBar = (MenuBar) primaryStage.getScene().lookup("#menuBar");
        cmbSlots = (ComboBox) primaryStage.getScene().lookup("#cmbSlots");
        cmbColors = (ComboBox) primaryStage.getScene().lookup("#cmbColors");
        buildMenu();
        // Handlers
        attachAction(btnBack, Action.BACK);
        attachAction(btnSettings, Action.SETTINGS);
        btnPrimaryAction.setOnAction(event -> primaryAction.execute());

        // Startup actions
        startFromFragment(gameFragment);

        // Initial events
        onLoggedInChanged();


    }

    // Actions
    public enum Action {
        BACK(instance::actionBack, "Back"),
        LOGIN(instance::actionLogin, "Log in"),
        NEW_GAME(instance::actionNewGame, "New Game"),
        LOAD_SAVED_GAME(instance::actionLoadSavedGame, "Load saved game"),
        EDIT_PLAYERS(instance::actionEditPlayers, "Edit players"),
        NEW_PLAYER(instance::actionNewPlayer, "New player"),
        SETTINGS(instance::actionSettings, "Settings"),
        RANKING(instance::actionRanking, "Ranking"),
        TEST_REGISTER_AND_LOGIN(instance::actionTestRegisterAndLogin, "Register and login"),
        TEST_START_GAME(instance::actionTestCreatePlayerAndStartGame, "Create enemy and start game"),
        TEST_LOGIN_AND_PLAY(instance::actionTestLoginAndPlay, "Login and play"),
        EXIT(instance::actionExit, "Exit");

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

    public void attachAction(ButtonBase buttonBase, Action action) {
        buttonBase.setOnAction(event -> action.execute());
    }

    public void attachAction(MenuItem menuItem, Action action) {
        menuItem.setOnAction(event -> action.execute());
    }

    private void actionBack() {
        popFragment();
    }

    private void actionLogin() {
        pushFragment(new LoginFragment());
    }

    public void actionNewGame() {
        pushFragment(new NewGameFragment());
    }

    public void actionEditPlayers() {
        pushFragment(new PlayersFragment());
    }

    public void actionNewPlayer() {
        pushFragment(new NewPlayerFragment());
    }
    public void actionSettings() {
        pushFragment(new SettingsMenuFragment());
    }
    public void actionRanking() {
        pushFragment(new RankingFragment());
    }

    public void actionTestRegisterAndLogin() {
        ServiceManager.getPlayersService().createHumanPlayer("biel", "1234");
        ServiceManager.getLoginService().login("biel", "1234");
        onLoggedInChanged();
    }

    public void actionTestCreatePlayerAndStartGame() {
        if (!ServiceManager.getLoginService().isLoggedIn()) return;
        ServiceManager.getPlayersService().createMinmiaxAIPlayer("TestMinimax", 14);

        GameService gameService = ServiceManager.getGameService();
        gameService.setMaxTrialCount(2);
        gameService.setSlotCount(7);
        gameService.setColorCount(6);
        gameService.setLocalStartsMakingCode(true);
        gameService.newGame(0);
        gameFragment.onResume();
    }

    public void actionTestLoginAndPlay() {
        actionTestRegisterAndLogin();
        actionTestCreatePlayerAndStartGame();
    }

    public void actionExit() {
        Platform.exit();
    }


    private void actionLoadSavedGame() {
        // TODO Open saved game fragment

    }

    public void onLoggedInChanged() {
        LoginService loginService = ServiceManager.getLoginService();
        String loggedInText = "Not logged in";
        if (loginService.isLoggedIn()) {
            loggedInText = "Logged in as " + loginService.getLoggedInPlayerName();
        }
        lblLoggedIn.setText(loggedInText);
        updatePrimaryAction();
    }

    public void setPrimaryAction(Action primaryAction) {
        btnPrimaryAction.setText(primaryAction.getDisplayName());
        this.primaryAction = primaryAction;
    }

    public void updatePrimaryAction() {
        LoginService loginService = ServiceManager.getLoginService();
        if (!loginService.isLoggedIn()) {
            setPrimaryAction(Action.LOGIN);
            return;
        }
        if (!inGame) {
            setPrimaryAction(Action.NEW_GAME);
            return;
        }
    }


    // Fragments
    public void pushFragment(Fragment fragment) {
        if (isCurrentFragment(fragment.getClass())) {
            swapFragment(fragment);
            return;
        }
        fragmentStack.push(fragment);
        displayTopOfStack();
    }

    public void popFragment() {
        if (fragmentStack.size() <= 1) return;
        fragmentStack.pop();
        displayTopOfStack();
    }

    public void swapFragment(Fragment fragment) {
        fragmentStack.pop();
        pushFragment(fragment);
    }

    public void startFromFragment(Fragment fragment) {
        fragmentStack.clear();
        pushFragment(fragment);
    }

    private void displayTopOfStack() {
        Fragment fragment = fragmentStack.lastElement();
        displayFragment(fragment);
    }

    private void displayFragment(Fragment fragment) {
        fragment.displayIn(pnlContent);
        fragment.onResume();
    }

    private boolean isCurrentFragment(Class<? extends Fragment> clazz) {
        if (fragmentStack.empty()) return false;
        return fragmentStack.lastElement().getClass().isAssignableFrom(clazz);
    }

    private void buildMenu() {
        Menu menuGame = new Menu("Game");
        addMenuItem(menuGame, Action.NEW_GAME);
        addMenuItem(menuGame, Action.LOAD_SAVED_GAME);
        addMenuSeparator(menuGame);
        addMenuItem(menuGame, Action.SETTINGS);
        addMenuSeparator(menuGame);
        addMenuItem(menuGame, Action.EXIT);

        Menu menuPlayers = new Menu("Players");
        addMenuItem(menuPlayers, Action.EDIT_PLAYERS);
        addMenuItem(menuPlayers, Action.NEW_PLAYER);
        addMenuSeparator(menuPlayers);
        addMenuItem(menuPlayers, Action.RANKING);


        Menu menuTest = new Menu("Test");
        addMenuItem(menuTest, Action.TEST_REGISTER_AND_LOGIN);
        addMenuItem(menuTest, Action.TEST_START_GAME);
        addMenuSeparator(menuTest);
        addMenuItem(menuTest, Action.TEST_LOGIN_AND_PLAY);

        menuBar.getMenus().addAll(menuGame, menuPlayers, menuTest);

    }

    private void addMenuItem(Menu menu, Action action) {
        MenuItem item = new MenuItem(action.getDisplayName());
        attachAction(item, action);
        menu.getItems().add(item);
    }

    private void addMenuSeparator(Menu menu) {
        menu.getItems().add(new SeparatorMenuItem());
    }

    private void displayExceptionAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, e.getMessage());
        alert.show();
    }

    public static void main(String[] args) {
        // Initialize repositories with an in-memory implementation
        RepositoryManager.attachImplementation(new RepositoriesInMemoryImpl());
        // Launch JavaFX application
        javafx.application.Application.launch(args);
    }
}
