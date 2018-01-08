package com.mastermind.ui.javafx;

import com.mastermind.model.entities.types.HumanPlayer;
import com.mastermind.services.ServiceManager;
import com.mastermind.services.login.responses.LoginResponse;
import com.mastermind.services.players.responses.CreatePlayerResponse;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

public class LoginFragment extends Fragment {
    private TextField txtUserName;
    private TextField txtPassword;
    private Label lblMessages;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onLoad() {
        txtUserName = (TextField) lookup("#txtUserName");
        txtPassword = (TextField) lookup("#txtPassword");
        lblMessages = (Label) lookup("#lblMessages");
        btnLogin = (Button) lookup("#btnLogin");
        btnRegister = (Button) lookup("#btnRegister");

        EventHandler<ActionEvent> actionLoginHandler = event -> actionLogin();
        txtUserName.setOnAction(actionLoginHandler);
        txtPassword.setOnAction(actionLoginHandler);
        btnLogin.setOnAction(actionLoginHandler);
        btnRegister.setOnAction(e -> actionRegister());
    }
    private void actionLogin(){
        String enteredUsername = txtUserName.getText();
        String enteredPassword = txtPassword.getText();
        if(enteredUsername.isEmpty()){
            lblMessages.setText("");
            return;
        }
        LoginResponse response =
                ServiceManager.getLoginService().login(enteredUsername, enteredPassword);
        if (response.isSuccess()) {
            lblMessages.setText("Login success");
            getApplication().updateLoggedInLabel();
            close();
        } else {
            if (!response.getMessages().isEmpty())
                lblMessages.setText(response.getMessages().get(0));
            txtPassword.setText("");
        }
    }
    private void actionRegister(){
        getApplication().pushFragment(new RegisterFragment());
    }
}
