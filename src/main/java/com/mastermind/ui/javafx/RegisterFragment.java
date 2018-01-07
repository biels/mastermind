package com.mastermind.ui.javafx;

import javafx.scene.control.*;

public class RegisterFragment extends Fragment{
    TextField txtUserName;

    @Override
    protected void onLoad() {
        txtUserName = (TextField) lookup("#txtUserName");

        txtUserName.setText("Loaded text");
    }
}
