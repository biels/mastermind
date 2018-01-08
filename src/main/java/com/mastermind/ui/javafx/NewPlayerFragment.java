package com.mastermind.ui.javafx;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Random;

public class NewPlayerFragment extends Fragment{
    ChoiceBox cmbType;
    Label lblDepth, lblSeed;
    TextField txtDepth, txtSeed;
    int lastSelectedIndex = 0;
    @Override
    protected void onLoad() {
        lblDepth = (Label) lookup("#lblDepth");
        lblSeed = (Label) lookup("#lblSeed");
        txtDepth = (TextField) lookup("#txtDepth");
        txtSeed = (TextField) lookup("#txtSeed");
        cmbType = (ChoiceBox) lookup("#cmbType");
        cmbType.setItems(FXCollections.<String>observableArrayList("Random", "Minimax"));
        cmbType.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int previousIndex = oldValue.intValue();
            int selectedIndex = newValue.intValue();
            onSelectionChange(previousIndex, selectedIndex);
        });
        onSelectionChange(1, 0);
        Random random = new Random();
        txtSeed.setText(String.valueOf(random.nextInt(400000)));
        txtDepth.setText(String.valueOf(random.nextInt(15)));
    }

    private void onSelectionChange(int previousIndex, int selectedIndex) {
        if (selectedIndex == 0){
            lblSeed.setVisible(true);
            txtSeed.setVisible(true);
        }
        if (previousIndex == 0){
            lblSeed.setVisible(false);
            txtSeed.setVisible(false);
        }
        if (selectedIndex == 1){
            lblDepth.setVisible(true);
            txtDepth.setVisible(true);
        }
        if (previousIndex == 1){
            txtDepth.setVisible(false);
        }
        lastSelectedIndex = selectedIndex;
    }
}
