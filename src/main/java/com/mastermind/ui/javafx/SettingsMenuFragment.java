package com.mastermind.ui.javafx;

import com.mastermind.services.ServiceManager;
import com.mastermind.services.game.GameService;
import com.mastermind.services.game.responses.types.UserGameState;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;

public class SettingsMenuFragment extends Fragment {
    ComboBox cmbElementCount, cmbSlotCount, cmbMaxTrials, cmbRoundCount;
    ToggleButton chkStartingRole, chkAllowElementRepetition;
    Button btnApply, btnCancel, btnLoad;
    @Override
    protected void onLoad() {
        cmbElementCount = (ComboBox) lookup("#cmbElementCount");
        cmbSlotCount = (ComboBox) lookup("#cmbSlotCount");
        cmbMaxTrials = (ComboBox) lookup("#cmbMaxTrials");
        cmbRoundCount = (ComboBox) lookup("#cmbRoundCount");
        chkStartingRole = (ToggleButton) lookup("#chkStartingRole");
        chkAllowElementRepetition = (ToggleButton) lookup("#chkAllowElementRepetition");
        btnApply = (Button) lookup("#btnApply");
        btnCancel = (Button) lookup("#btnCancel");
        btnLoad = (Button) lookup("#btnLoad");
        btnApply.setOnAction(event -> {
            saveSettings();
            close();
        });
        btnCancel.setOnAction(event -> {
            close();
        });
        btnLoad.setOnAction(event -> {
            loadSettings();
        });
        fillMenus();
        loadSettings();
    }

    @Override
    protected void onResume() {
        loadSettings();
    }

    private void fillMenus() {
        for (int i = 2; i <= 8; i++) {
            cmbSlotCount.getItems().add(i + " slots");
        }
        for (int i = 2; i <= 7; i++) {
            cmbElementCount.getItems().add(i + " colors");
        }
        for (int i = 2; i < 15; i++) {
            cmbMaxTrials.getItems().add(i + " trials");
        }
        for (int i = 2; i < 20; i++) {
            cmbRoundCount.getItems().add(i + " rounds");
        }
        chkStartingRole.selectedProperty().addListener((observable, oldValue, newValue) ->
                chkStartingRole.setText(newValue ? "Codemaker" : "Codebreaker"));
        chkAllowElementRepetition.selectedProperty().addListener((observable, oldValue, newValue) ->
                chkAllowElementRepetition.setText(newValue ? "Enabled" : "Disabled"));

    }

    private void loadSettings(){
        GameService gameService = ServiceManager.getGameService();
        UserGameState state = gameService.getUserGameState();
        selectValue(cmbElementCount, state.getColorCount(), 2);
        selectValue(cmbSlotCount, state.getSlotCount(), 2);
        selectValue(cmbMaxTrials, state.getMaxTrialCount(), 2);
        selectValue(cmbRoundCount, state.getTotalRoundCount(), 2);
        chkStartingRole.setText(state.isLocalStartsMakingCode() ? "Codemaker" : "Codebreaker");
        chkStartingRole.setSelected(state.isLocalStartsMakingCode());
        chkAllowElementRepetition.setText(state.isAllowRepetition() ? "Enabled" : "Disabled");
        chkAllowElementRepetition.setSelected(state.isAllowRepetition());
    }
    private void saveSettings(){
        GameService gameService = ServiceManager.getGameService();
        gameService.setColorCount(readValue(cmbElementCount, 2));
        gameService.setSlotCount(readValue(cmbSlotCount, 2));
        gameService.setMaxTrialCount(readValue(cmbMaxTrials, 2));
        gameService.setRoundCount(readValue(cmbRoundCount, 2));
        gameService.setLocalStartsMakingCode(chkStartingRole.isSelected());
        gameService.setAllowRepetition(chkAllowElementRepetition.isSelected());
    }
    private void selectValue(ComboBox comboBox, int value , int offset){
        comboBox.getSelectionModel().select(value - offset);
    }
    private int readValue(ComboBox comboBox, int offset){
        return comboBox.getSelectionModel().getSelectedIndex() + 2;
    }
}
