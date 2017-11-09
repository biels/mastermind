package com.mastermind.ui.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SampleWindowController {
    @FXML
    private TextField text1;

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        text1.setText(Integer.toString(Integer.parseInt(text1.getText()) + 1));
    }
}
