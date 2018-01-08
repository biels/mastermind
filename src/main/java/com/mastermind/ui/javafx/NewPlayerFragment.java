package com.mastermind.ui.javafx;


import com.mastermind.model.entities.types.MinimaxAIPlayer;
import com.mastermind.model.entities.types.RandomAIPlayer;
import com.mastermind.services.ServiceManager;
import com.mastermind.services.players.PlayersService;
import com.mastermind.services.players.responses.CreatePlayerResponse;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class NewPlayerFragment extends Fragment {
    ChoiceBox cmbType;
    Label lblDepth, lblSeed, lblMessages;
    TextField txtDepth, txtSeed, txtName;
    Button btnCreate;
    PlayersService playersService;
    int lastSelectedIndex = 0;

    @Override
    protected void onLoad() {
        playersService = ServiceManager.getPlayersService();
        lblDepth = (Label) lookup("#lblDepth");
        lblSeed = (Label) lookup("#lblSeed");
        lblMessages = (Label) lookup("#lblMessages");
        txtDepth = (TextField) lookup("#txtDepth");
        txtSeed = (TextField) lookup("#txtSeed");
        txtName = (TextField) lookup("#txtName");
        cmbType = (ChoiceBox) lookup("#cmbType");
        btnCreate = (Button) lookup("#btnCreate");
        btnCreate.setOnAction(event -> onCreate());
        cmbType.setItems(FXCollections.<String>observableArrayList("Random", "Minimax"));
        cmbType.setValue("Random");
        cmbType.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int previousIndex = oldValue.intValue();
            int selectedIndex = newValue.intValue();
            onSelectionChange(previousIndex, selectedIndex);
        });
        onSelectionChange(1, 0);
        Random random = new Random();
        txtSeed.setText(String.valueOf(random.nextInt(400000)));
        txtDepth.setText(String.valueOf(random.nextInt(15)));
        txtName.setText(generateRandomName());
    }

    private void onSelectionChange(int previousIndex, int selectedIndex) {
        if (selectedIndex == 0) {
            lblSeed.setVisible(true);
            txtSeed.setVisible(true);
        }
        if (previousIndex == 0) {
            lblSeed.setVisible(false);
            txtSeed.setVisible(false);
        }
        if (selectedIndex == 1) {
            lblDepth.setVisible(true);
            txtDepth.setVisible(true);
        }
        if (previousIndex == 1) {
            lblDepth.setVisible(false);
            txtDepth.setVisible(false);
        }
        lastSelectedIndex = selectedIndex;
    }

    private String generateRandomName() {
        Random random = new Random();
        List<String> adjectives1 = Arrays.asList("Total", "Ultimate", "Extreme", "Complete", "Perfect", "Supreme", "Quantum", "Serial");
        List<String> middles = Arrays.asList("Game", "Match");
        List<String> adjectives2 = Arrays.asList("Destroyer", "Terminator", "Finisher", "Crusher", "Devastator", "Guesser", "MindReader", "Champion");
        List<String> endings = Arrays.asList("Machine", "Agent", "Mind");
        String adjective1 = adjectives1.get(random.nextInt(adjectives1.size()));
        String middle = middles.get(random.nextInt(middles.size()));
        String adjective2 = adjectives2.get(random.nextInt(adjectives2.size()));
        String ending = endings.get(random.nextInt(endings.size()));
        BiFunction<String, Integer, String> chance = (s, c) -> random.nextInt(c) <= 10 ? s : "";
        return chance.apply("The", 100) + chance.apply(adjective1, 12) + chance.apply(middle, 160) + adjective2 + chance.apply(ending, 120);
    }

    private void onCreate() {
        String enteredName = txtName.getText();
        if (lastSelectedIndex == 0) {
            String enteredSeed = txtSeed.getText();
            if (!NumberUtils.isNumber(enteredSeed)) {
                lblMessages.setText("Seed must be a number");
                return;
            }
            Long seed = Long.valueOf(enteredSeed);
            CreatePlayerResponse<RandomAIPlayer> response = playersService.createRandomAIPlayer(enteredName, seed);
            if (response.isSuccess()) {
                close();
            } else {
                if (!response.getMessages().isEmpty()) lblMessages.setText(response.getMessages().get(0));
            }
        }
        if (lastSelectedIndex == 1) {
            String enteredDepth = txtDepth.getText();
            if (!NumberUtils.isNumber(enteredDepth)) {
                lblMessages.setText("Depth must be a number");
                return;
            }
            CreatePlayerResponse<MinimaxAIPlayer> response = playersService.createMinmiaxAIPlayer(enteredName, Integer.parseInt(enteredDepth));
            if (response.isSuccess()) {
                close();
            } else {
                if (!response.getMessages().isEmpty()) lblMessages.setText(response.getMessages().get(0));
            }
        }
    }
}
