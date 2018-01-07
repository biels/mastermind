package com.mastermind.ui.javafx;

import com.mastermind.model.entities.types.HumanPlayer;
import com.mastermind.services.ServiceManager;
import com.mastermind.services.players.responses.CreatePlayerResponse;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RegisterFragment extends Fragment {
    private TextField txtUserName;
    private TextField txtPassword;
    private Label lblMessages;
    private Button btnRegister;

    @Override
    protected void onLoad() {
        txtUserName = (TextField) lookup("#txtUserName");
        txtPassword = (TextField) lookup("#txtPassword");
        lblMessages = (Label) lookup("#lblMessages");
        btnRegister = (Button) lookup("#btnRegister");

        EventHandler<ActionEvent> actionRegisterHandler = event -> actionRegister();
        txtUserName.setOnAction(actionRegisterHandler);
        txtPassword.setOnAction(actionRegisterHandler);
        btnRegister.setOnAction(actionRegisterHandler);
    }

    protected void actionRegister() {
        String enteredUsername = txtUserName.getText();
        String enteredPassword = txtPassword.getText();
        CreatePlayerResponse<HumanPlayer> response =
                ServiceManager.getPlayersService().createHumanPlayer(enteredUsername, enteredPassword);
        if (response.isSuccess()) {
            lblMessages.setText("Success");
            close();
        } else {
            if (!response.getMessages().isEmpty())
                lblMessages.setText(response.getMessages().get(0));
        }
    }
}
